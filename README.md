# Project-Mobile-Application-Development
Application to see your bank accounts

Personnal interpretations : 
For this exercise, I considered that the client has the same id than his account.
I also considered that the application will run in offline mode if the user is not connected to any network
or if he choose to use this mode. In that case, there will not be any exchanges with the api 
and we will use the stored data. Thus, if a client uses this mode, he can't update its account.


How you ensure user is the right one starting the app :
To make sure that the client who is trying to connect is the right one, I just ask him to
enter his id and I redirect him to the page with his account informations.


How do you securely save user's data on your phone :
To save user's data, I used SharedPreference. I have make a file for each account 
where the user has connected before by using the online mode.


How did you hide the API url :
I didn't find a good way to hide an url so I hardcoded it.
