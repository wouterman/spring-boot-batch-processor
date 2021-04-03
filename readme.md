# Batch processor with Spring Boot
This is a sample project to compare two implementations of processing large amounts of records.  
Both implementations process two files with 1 million records each.
One implementation processes each record one by one using a query every time to look up each record in the database.  
The other one processes all records and then pages through the database to filter out the duplicates.

## Results
The first one (RegularRegistrationProcessor) takes 20 seconds to process the first file and hangs on the second file.
The second one (HashMapRegistrationprocessor) processed both files in ~9 seconds.