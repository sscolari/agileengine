# agileengine

Santiago Scolari

How to run:
java -cp exercise.jar com.agileengine.ButtonFinder src/main/resources/samples/sample-0-origin.html src/main/resources/samples/sample-1-evil-gemini.html

I include all provided test cases in the folder src/main/resources/samples 

The program receive two mandatory parameters and one optional one.
Param1 -> original file path
Param2 -> target file
Param3 (optional) -> original file id

For the solution I decided to use a system of scores for each attribute in the html link. 
I think that 'class' attribute is the most important over 'title' and 'href'. Class has the higher score, and the other two lower ones. I didn't have time to make much tests. I think the provided cases are working, buy maybe the scores need to be adjusted in order to run in more use cases.
