**Zombie Apocalypse**

**Running**
- Import the pom.xml into an IDE as a Maven project
- Build using maven
- Run (or debug) from the IDE

**Instructions**
- By default, if no command line argument is provided it will load the resource file "Input.txt" and use that to create the Zombie World.
- A command line argument can be provided, which is the absolute path to an input file. For example:
"C:/Input Files/NewInput.txt" can be provided as a command line argument.

**Notes**
- If the input file is correct then the results will be shown on the console in the form: 
`zombies score: 3`
 `zombies positions:
 (3,0)(2,1)(1,0)(0,0)`
- If the input file is incorrect then the Zombie World won't be created and there will be errors in logs.