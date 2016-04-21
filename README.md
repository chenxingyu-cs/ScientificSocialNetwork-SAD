# ScientificSocialNetwork

Both frontend and backend


HOW TO IMPORT:

1. git clone the project

2. If you want to use IDE, use "activator ui" and import both backend and frontend, generate the ide code.

3. Use "git status" to check the status of github, if you modified the "build.sbt", please "git checkout build.sbt", if there are other differences, please contact me with Slack or Email

4. Import the code into the IDE


HOW TO CONFIGURE:

1. To avoid confilting with the existing database, please first create a new database named "scientificSN2" in MySQL

2. The username and password are all "root"

3. I will later upload a database dump file which contain the data extracted from dblp.xml


HOW TO RUN:

1. Go to the frontend folder and run './bin/activator "run 9068" '

2. Go to the backend folder and run './bin/activator "run 9069" '

3. Notice that there is a "./bin" this time which is due to the difference of version of Play

4. Then the backend code will generate the table automaticly



HOW TO CODE:

1. There are some difference between this version and the last version

2. For backend: The biggest difference is using EBean instead of Spring, you can check that on http://ebean-orm.github.io/

3. For frontend: The biggest difference is how to communicate with backend, you can check my code in "getAllPublications" to know how to get it.

4. For both backend and frontend, remember to delete useless part( Ebean-related parts )


Last, sorry for the delay and inconvenience, if you hava any problems, contact me on Slack, Email or Wechat~


