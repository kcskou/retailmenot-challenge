#RetailMeNot challenge

My proposed solution to the challenge presented by RetailMeNot, Inc. on a Mindsumo contest.

#Challenge prompt

Linked below is 1 hour of web access logs from www.retailmenot.com. Answer the following questions using a programming language of your choice among C#, Java, Python, Ruby or PHP. Extra credit for answering each question in a different language and for not using 3rd party libraries.

The purpose of this challenge is to code the solution using only basic data structures. You should not simply load the data into a database or use LINQ.

Provide all code in a ZIP file with relevant release notes and comments to best demonstrate your ability and thought process while solving these problems.

Http Access Log Sample from retailmenot.com: https://db.tt/dv8Z2wbv

1. Clicks out from retailmenot.com to a merchant are important to us. They can be found in the logs by looking for GET requests to /out/{couponId}. Provide descriptive statistics about the number of clicks we see per minute. Specifically, calculate the minimum, maximum, mean, median and standard deviation of clicks out per minute. For minimum and maximum, also compute the minute when the value occurred. Generate a csv file of the that contains this statistics.

2. Much of our traffic is a GET request to a store page. Store pages are denoted in the log by a GET request to /view*/{store domain} where * is either empty or any integer value. Examples of this path pattern are /view14/bestbuy.com and /view/gap.com. Calculate the Bounce Rate for each store page. You can assume visitors are uniquely identified by their IP. You can consider the 1 hour of weblogs as a single session, i.e. any user who visited a single page in the logs should be considered a bounce.

See: wikipedia.org/wiki/Bounce_rate. "A bounce occurs when a web site visitor only views a single page on a website, that is, the visitor leaves a site without visiting any other pages before a specified session ­timeout occurs. There is no industry standard minimum or maximum time by which a visitor must leave in order for a bounce to occur. Rather, this is determined by the session timeout of the analytics tracking software.

Rb = Tv / Te 
where 
Rb = Bounce rate 
Tv = Total number of visitors viewing one page only 
Te = Total entries to page


#My proposal

I answered the two questions using Java and Python respectively. For question one, I decided to take this opportunity to experiment with object oriented design in Java, so my code was organized into a number of classes. In contrast, I answered question two with a single Python script. In both cases, only the standard libraries that come with the language were used. I have documented all classes and methods and used comments where necessary to walk the reader through my code. I hope you will find that my code is readable and self-explanatory, but let me summarize my approach here as well. In both cases, my program does these five things:

1. Parse the Http Access Log Sample provided.

2. Filter the entries and keep only the relevant ones.

3. Use a hash table to group log entries.

4. Calculate relevant metrics for each group.

5. Save analysis results to a file.

Please refer to the attached source code for a more detailed explanation of how I solved these problems.

NB: In my calculations, I have excluded the single click out entry at 23:59:59 because we don't have data for the full minute of 23:59.


Direct link to the solution page on MindSumo: https://www.mindsumo.com/solutions/48468
