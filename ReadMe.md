<snippet>
  <content>
   <h1>GauchSafe</h1> 
    
    
A location-based service for UCSB students to covertly report danger and ask for help simply by shaking their cell phone. Developed for STKO Lab at UCSB, this project has two separate components, an Android App and a Web portal.

Users can trigger the app by shaking their phone when they feel unsafe, and the app will report their location anonymously to campus officials or UCSB police department. A neural network was deployed to detect shakes from normal movement.

![Image of Android interface]
(https://github.com/behzad-vahedi/GauchoSafe/blob/master/images/GauchoSafe1.png)


In the web portal, the location data submitted by users are used to create a heat map of unsafe locations so that officials can take propoer measures in those areas.

• Technology Used:

Android,</br>
Python (Scikit-learn),</br>
HTML,</br>
CSS,</br>
PHP,</br>
Leaflet.js

<h2>Installation</h2>
To get in running, you need to: 

in Server folder
- Create a Mysql database using db.sql file (in sql folder).
- Modify config.php in folder web, and set the database info in it.
- Modify db.php in v1 folder and set your database info there.

In Android folder
- Modify Confing.java and set the base_url variable to your server location (or localhost).


    </content>
  <tabTrigger>readme</tabTrigger>
</snippet>
