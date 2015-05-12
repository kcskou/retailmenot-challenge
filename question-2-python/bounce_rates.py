#!/usr/bin/env python

import os
import re
import csv

## Constants

WEBLOG_FILEPATH = "input/rmn_weblog_sample.log"
OUTPUT_PATH = "output/bounce_rates.csv"

## Functions

def main():
    """
    Calculate the Bounce Rate for each store page.
    """
    print "Parsing web log..."
    log = parse_weblog(WEBLOG_FILEPATH)
    print "Keeping only store page entries..."
    store_page_entries = keep_only_store_page(log)
    print "Grouping entries by domain..."
    store_pages = hash_entries(store_page_entries) 
    print "Calculating bounce rates for each store page..."
    bounce_rates = compute_bounce_rate(store_pages)
    print "Saving results to file..."
    save_as_csv(bounce_rates, OUTPUT_PATH)
    
def parse_weblog(path):
    """
    Parse the weblog and return a generator of HTTP access log entries.
    """
    fullpath =  os.path.abspath(path)
    with open(fullpath, 'rb') as f:
        for line in f:
            yield make_entry(line)

def make_entry(line):
    """
    Return a dictionary that represents an entry of the weblog.
    
    Test case:
    >>> test_line = '97.29.167.176 - - [22/Sep/2011:00:02:20 +0000] "POST /ajax/savings.php HTTP/1.1" 200 69 "http://www.retailmenot.com/view/landsend.com" "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2'
    >>> entry = make_entry(test_line)
    >>> entry["visitor_id"]
    '97.29.167.176'
    >>> entry["method"]
    'POST'
    >>> entry["uri"]
    '/ajax/savings.php'

    """
    #focus on relevant parts
    parts = line.split(" - ")
    visitor_id = parts[0]
    subparts = parts[1].split('"')
    method_and_uri = subparts[1]
    method_and_uri_parts = method_and_uri.split(" ")
    method = method_and_uri_parts[0]
    uri = method_and_uri_parts[1]
    d = dict()
    d["visitor_id"] = visitor_id
    d["method"] = method
    d["uri"] = uri
    return d

def keep_only_store_page(log):
    """
    Returns a generator that only produces store page entries.
    """
    for entry in log:
        if is_store_page(entry):
            yield entry

def is_store_page(entry):
    """
    Return true if entry is a GET request to /view*/{store domain}
    where * is either empty or any integer value, otherwise return false.

    Test case:
    >>> test_1 = {"visitor_id":"12.34.567.890", \
                  "method":"GET", \
                  "uri":"/view/example.com"}
    >>> test_2 = {"visitor_id":"88.64.563.153", \
                  "method":"POST", \
                  "uri":"/view/example.com"}
    >>> test_3 = {"visitor_id":"24.13.544.772", \
                  "method":"GET", \
                  "uri":"/view231/example.com"}
    >>> test_4 = {"visitor_id":"12.65.231.464", \
                  "method":"GET", \
                  "uri":"/ajax/abc.com"}
    >>> map(is_store_page, [test_1, test_2, test_3, test_4])
    [True, False, True, False]

    """
    pattern = re.compile("^/view\d*/.*$")
    return entry["method"] == "GET" and pattern.match(entry["uri"]) != None

def hash_entries(entries):
    """
    Return a dictionary with unique store pages as keys and the corresponding
    lists of visitor_ids.
    """
    d = dict()
    for e in entries:
        uri = e["uri"]
        domain = re.match("^/view\d*/(.*)$", uri).group(1)
        if domain:
            visitor_id = e["visitor_id"]
            if d.has_key(domain):
                store_page_entries = d[domain]
                store_page_entries.append(visitor_id)
            else:
                d[domain] = [visitor_id]
    print "Retrieved {0} unique domains.".format(len(d))
    return d

def compute_bounce_rate(store_pages):
    """
    Returns a tuple that contains the bounce rate, total count of visitors
    who only viewed a single page, and total count of visits for
    a store page.
    """
    d = dict()
    for sp in store_pages.iteritems():
        domain = sp[0]
        visitors_list = sp[1]
        
        # count page visited for each visitor
        visit_counts = dict()
        for v in visitors_list:
            if visit_counts.has_key(v):
                visit_counts[v] += 1
            else:
                visit_counts[v] = 1

        # count visitors who viewed only one page
        total_single_page_viewers = sum(
                1 for vc in visit_counts.itervalues() if vc == 1)

        total_visits = len(visitors_list)

        # calculate bounce rate
        bounce_rate = total_single_page_viewers / float(total_visits)
        d[domain] = (bounce_rate, total_single_page_viewers, total_visits)
    return d

def save_as_csv(bounce_rates, path):
    """
    Save analysis output to a csv file.
    """
    fieldnames = ["store_page_domain", "bounce_rate",
    "total_single_page_viewers", "total_visits"]
    rows = [{fieldnames[0]:k, \
             fieldnames[1]:v[0], \
             fieldnames[2]:v[1], \
             fieldnames[3]:v[2]} for k, v in bounce_rates.iteritems()]

    fullpath = os.path.abspath(path)
    with open(fullpath, 'wb') as f:
        writer = csv.DictWriter(f,fieldnames)
        writer.writeheader()
        writer.writerows(rows)
    print 'Successfully saved output to "{0}".'.format(OUTPUT_PATH)

if __name__ == "__main__":
    import doctest
    doctest.testmod()
    main()
