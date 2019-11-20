# docTalk
app that helps docs writing


To run the app on and all the additional tools Windows 10 with tests do the following:
1. Download sphinxbase, pocketsphinx, sphinxtrain and sphinx and install this packages in one folder. 
2. Change the name and leave just the name of the package without any version info.
3. In sphinx-model-trainer/application.yaml provide the path to the package with sphinxtools in sphinx.directory property
4. Download US English model in https://sourceforge.net/projects/cmusphinx/files/Acoustic%20and%20Language%20Models/ 
(you can download also other models) and put it in resources folder in sphinx-model-trainer module on package com.subtilitas.doctalk.adapter.service.en-us. 
The last package is the name of the model provided in SpeechModelInitializer
5. Copy whole test resources package  od com.subtilitas.doctalk.adapter.service content to main resources package com.subtilitas.doctalk.init.verification (The file with text put to init package, one level higher)
6. Run command mvn clean install to build the project. And run class with main function SphinxModelTrainerApplication.
7. The back-end server is now running on localhost:8000.
8. go to  model-trainer-app module and enter npm install to build the front-end (Node.js and npm is required)
9. to run the front-end server on localhost:3000 enter npm run in the console.

Congratulations, you ran the app!. 
#
I know it is a lot of effort to run the app but I will be working on automatization in the nearest future.