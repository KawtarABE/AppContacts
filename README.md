# AppContacts
The AppContacts Android Project serves as a robust professional contact management solution, tailored for Android users. AppContacts is designed to simplify networking experiences by offering intelligent recommendations, favoriting options, notes, seamless communication through calls and messages.

## Table of Contents
* [General Information](#general-information)
* [Setup](#setup)
* [Usage](#usage)
* [Screenshots](#screenshots)


## General Information
### Main features
* Recommendation Engine: Discover relevant professionals effortlessly.
* Favoriting: Prioritize and keep track of key contacts.
* Notes and Annotations: Capture important details for each contact.
* Communication Hub: Make calls and send messages within the app.


### Technologies Used
[![PHP](https://skillicons.dev/icons?i=android,firebase)](https://skillicons.dev)



## Setup
### Pre-Requisites
To set up this project you should install the following:
- XAMPP (apache, mysql)
- VSCODE (or any other IDE)

### Usage
To use the project you should do the following:
- Add the project to **'htdocs'** folder on the xampp environment.
- Create the database on phpmyadmin panel.
  * patients(**`id`**, **`email`**, **`password`**, **`first_name`**, **`last_name`**, **`sexe`**, **`date_of_birth`**)
  * maladies(**`id_maladie`**, **`name_of_maladie`**)
  * cathegories(**`id_cathegory`**, **`name_of_cathegory`**)
  * patient_maladie(**`id_patient`**, **`id_maladie`**)
  * forum(**`id`**, **`Description`**, **`Post`**, **`patient`**, **`id_categorie`**, **`statut`**, **`type`**)
  * answers(**`id_consultation`**, **`id_doctor`**, **`answer`**)
  * admins(**`id_admin`**, **`email`**, **`password`**, **`first_name`**, **`last_name`**)
  * doctors(**`id_doctor`**, **`email`**, **`password`**, **`first_name`**, **`last_name`**, **`sexe`**, **`id_cathegory`**)

## Screenshots
### Home Page
![Example screenshot](Screenshots/home.png)
### Admin dashboard 
![Example screenshot](Screenshots/Dashboard.png)
### Blog page
![Example screenshot](Screenshots/Blog.png)
### Consultations page
![Example screenshot](Screenshots/consultations.png)
