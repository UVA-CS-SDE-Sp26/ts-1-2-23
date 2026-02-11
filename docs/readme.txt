The program should be run from the class TopSecret… e.g., using the command:
 java topsecret

When the program runs with no arguments, it should list the numbered files available to display. E.g.:

01 filea.txt
02 fileb.txt
03 filec.txt

When the program runs with a number as an argument, the contents of the corresponding file is displayed on screen.

 java topsecret 01
(this displays on screen the contents of the file filea.txt)
The program exits after showing the file list, the contents of an indicated file, or after displaying an error message.

RESPONSIBILITIES:
Michael (Team member A) -> create and document the User Interface
Nassib (Team member B) -> create and document the File Handler
Mofe (Team member C) -> create and document the Program Control
Bairon (Team member D) -> creating and documenting the Cipher feature

RUN INSTRUCTIONS:
Navigate to ts-1-2-23/src/main/java
Run javac TopSecret.java
Run java TopSecret [argument]
