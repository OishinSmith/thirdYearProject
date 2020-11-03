## Technical Specification

## 0. Table of Contents
- 0. Table of Contents.....1
- 1. Introduction.....2
   - 1.1 Overview.....2
   - 1.2 Glossary.....3
   - 1.3 Libraries Used.....4
- 2. System Architecture.....5
   - 2.1 User App.....6
      - 2.1.0 Login and Sign up.....6
      - 2.1.1 Add Button and ID Name.....6
      - 2.1.2 ID Button Generated.....6
      - 2.1.3 ID Information Stored in HashMap.....7
      - 2.1.4 Getting and Setting HashMap from Database.....7
      - 2.1.5 Generate Dynamic Buttons.....7
   - 2.2 Admin App.....7
      - 2.2.0 Login and Sign up.....7
      - 2.2.1 Add ID.....8
      - 2.2.2 Capture Image.....8
      - 2.2.3 Upload.....8
      - 2.2.4 NFC Transfer.....8
      - 2.2.5 Verify ID.....9
- 3. High-Level Design.....10
- 4. Problems and Resolutions.....12
   - 4.1 NFC.....12
   - 4.2 User Generated Buttons/Dynamically Generated Buttons.....12
   - 4.3 Design.....12
   - 4.4 Firebase Realtime Database.....13
   - 4.5 Local vs Online Storage of IDs.....13
   - 4.6 Splitting The App Into Two.....13
   - 4.7 PGP Security.....14
   - 4.8 Differentiating Between Users and Admins.....14
- 5. Installation Guide.....15
   - 5.1 Prerequisites.....15
   - 5.2 Enabling APKs.....15
   - 5.3 Installing the Apps.....15
- 6. Known Problems.....16

## 1. Introduction

We have decided to do our project on identity documents (IDs) as having a virtual version of
said documents can prove useful in multiple scenarios, and at the same time this area hasn’t
been fully exploited on the Google Play Store.
The main advantages are:
* It would be convenient to have a virtual version of your IDs on your smart device, as
you wouldn’t have to take them out of your physical wallet.
* In some scenarios you might prefer to keep your wallet in a safe place, instead of
carrying it with you.
* Some people might simply prefer not to carry their IDs with themselves, as these
documents are more valuable in some countries than in others.
* In the case where a person has a virtual and physical ID, and they lose either one,
they can still use whichever ID is left, instead of being left with nothing.

### 1.1 Overview

The product that has been developed is a system of two Android applications working
together, each with its own unique important role. At first this product was intended to consist
of one application, but it has been split into two, for reasons that will be explained later. From
now on, these applications will be referred to as User app and Admin app. The product’s
main functionality and purpose is to provide a platform where the user is able to store and
use a digital version of their physical identity documents. Multiple IDs may be stored on the
User app, and they can be in any legitimate form, examples include: age cards, passports,
driving licences, citizen cards, and national identity cards.
Near Field Communication (NFC) is used for communication between the two apps,
therefore the smartphones used must support NFC, otherwise proper usage of our apps
won’t be possible. An internet connection is also necessary when using either of the apps.
Adding an ID to the User app happens by first showing the ID in question to someone with
access to the Admin app. The person with the Admin app starts the process by clicking on
‘Add ID’, which prompts them to take an image of the ID, then upload it to our database, and
lastly, to transfer the image’s URL to the User, via NFC. Technically anyone is able to sign
up, and login to the Admin app to add an ID, but the ID will simply be deemed invalid,
because an authorised account must sign the image first. Without a valid (electronic)
signature, the image (ID) is invalid.

### 1.2 Glossary

* Near Field Communication (NFC): ​ is a set of communication protocols that enable
two electronic devices, one of which is usually a portable device such as a
smartphone, to establish communication by bringing them within 4 cm (1.6 in) of each
other.
* Electronic Signature: ​ refers to data in electronic form, which is logically associated
with other data in electronic form and which is used by the signatory to sign. This
type of signature provides the same legal standing as a handwritten signature as
long as it adheres to the requirements of the specific regulation it was created under.
    * In our case this is the unique User ID of an account, that has been authorised
to sign images (IDs).
* Cloud Server: ​ A cloud server is a virtual server (rather than a physical server)
running in a cloud computing environment. It is built, hosted and delivered via a cloud
computing platform via the internet, and can be accessed remotely. They are also
known as virtual servers.
* HashMap ​: a Map based collection class that is used for storing key & value pairs, it
is denoted as HashMap<Key, Value> or HashMap<K, V>. This class makes no
guarantees as to the order of the map. It is similar to the Hashtable class except that
it is unsynchronized and permits nulls (null values and null key).
* ImageView: ​ in Android, you can use the ‘android.widget.ImageView’ class to display
an image file.
* Firebase: ​ is a mobile and web application development platform developed by
Firebase, Inc. in 2011, then acquired by Google in 2014. Firebase provides
numerous services such as a Realtime Database, Storage, Authentication, and
others.
    * Firebase Auth: ​ a service that can authenticate users using only client-side
code. It supports social login providers Facebook, GitHub, Twitter and Google
(and Google Play Games). Additionally, it includes a user management
system whereby developers can enable user authentication with email and
password login stored with Firebase.
    * Firebase Realtime Database: ​ Firebase provides a realtime database and
backend as a service. The service provides application developers an API
that allows application data to be synchronized across clients and stored in
Firebase's cloud.
    * Firebase Storage: ​ provides secure file uploads and downloads for Firebase
apps, regardless of network quality. The developer can use it to store images,
audio, video, or other user-generated content.
* Pretty Good Privacy (PGP): ​ is an encryption program that provides cryptographic
privacy and authentication for data communication. PGP is used for signing,
encrypting, and decrypting texts, e-mails, files, directories, and whole disk partitions
and to increase the security of e-mail communications.

### 1.3 Libraries Used
Libraries used in gradle and put in dependencies which helped in the development of ID-Wallet and which we take no credit for:
1. com.github.ittianyu:BottomNavigationViewEx:2.0.
2. com.android.support:design:28.0.
3. om.google.android.gms:play-services-auth:16.0.
4. com.android.support:appcompat-v7:28.0.
5. com.android.support:design:28.0.
6. com.android.support.constraint:constraint-layout:1.1.
7. com.google.firebase:firebase-database:16.1.
8. com.google.firebase:firebase-firestore:18.1.
9. com.google.firebase:firebase-storage:16.1.
10. com.google.firebase:firebase-auth:16.1.
11. junit:junit:4.
12. com.android.support.test:runner:1.0.
13. com.android.support.test.espresso:espresso-core:3.0.
14. com.github.bumptech.glide:compiler:4.9.
15. com.github.bumptech.glide:glide:4.9.
16. com.google.code.gson:gson:2.8.com.android.support.constraint:constraint-layout:1.1.

## 2. System Architecture

![System Architecture](https://i.imgur.com/94dyvzs.png)

### 2.1 User App
The user will first have to sign in to a google account, which stores an empty HashMap in the
database to be used afterwards. An if statement will be executed checking if the HashMap
inside the database is empty or not. If the HashMap in the database is not empty (has at
least one key and one value) then Buttons will be generated dynamically. If the HashMap in
the database is empty, then nothing happens.
Now that the Buttons have or have not been generated, the app will function as it normally
does. When a user Receives a Url pointing to their ID in the database, the user has the
option of naming the virtually stored ID as they wish.
Now that the user has named the ID, the add button will be pressed which adds the user
generated content, like the ID url reference and the user generated ID name, into a
HashMap. Now the button is generated and the HashMap is pushed to firebase realtime
Database.

#### 2.1.0 Login and Sign up

The login for the user is simple. Once the user opens up their app, they are presented with a
login screen. The user will be prompted to click on the “sign in” through google button. At this
stage, the user will choose a google account where all their IDs will be stored in.
When the User first signs in through google, their account user ID will be created
automatically and uploaded to firebase realtime database.

#### 2.1.1 Add Button and ID Name

After the user has signed in, the ID page will be displayed. Here, there is an “Add” button
and a text box displayed. The user may write text, to name the ID and when the Add button
is pressed two buttons are generated which are defined in the row.xml file and imported into
the customerMainView.class using an Inflator. Right after the buttons are generated, the
HashMap, called map, is updated and saved when the function save() is used. The function
save() will “set” the HashMap to the firebase Database reference, meaning that any
information under the current user will be overwritten.

#### 2.1.2 ID Button Generated

The ID button and corresponding Remove button are defined in the row.xml file. Now that
the ID button and remove button are generated, the user may use those buttons to remove
the ID or press the ID button to send that ID url to the Admin version of the App. As a way to
prevent the accidental deletion of IDs, there’s dialogue alert boxes that pop up when the
remove ID button is clicked.


#### 2.1.3 ID Information Stored in HashMap

Every time the user receives an Admin Url message, the locally used HashMap is saved and
pushed to the firebase Database. The locally stored HashMap contains a key, which is the
ID name, that is mapped to a value, which is the Users ID url. This HashMap is then stored
in firebase Database under each users account ID.

#### 2.1.4 Getting and Setting HashMap from Database

Each time the Locally used HashMap is updated, the save() function is executed. The save
function will set the local HashMap to the Database, in turn, the HashMap in the Database
will be overwritten every time the local HashMap is changed. There are two times that the
save function is executed. The first time it is executed is when the Add button is pressed,
and an ID is generated. The second time the save function is executed is when the remove
button is pressed.

#### 2.1.5 Generate Dynamic Buttons

The dynamically generated button are generated when the user first signs in. An if statement
is executed checking if the database for a HashMap. If the HashMap is empty, ie. “{ }”, then
no buttons will be generated. If the HashMap has values, ie.
“{“passport”:”​www.firebase.com​”}, then each key and corresponding value will be set to a
button and generated.

### 2.2 Admin App

As can be seen from the diagram the system consists of multiple functions. On the main
screen of the app, the options to Verify ID and Add ID are available. As explained in the
overview, adding an ID consists of taking an image of the document, then uploading it, and
sending its URL through NFC to the User app. The transfer of the URL isn’t shown in the
diagram for simplicity purposes, but technically, it would be another function underneath
‘Upload’.

#### 2.2.0 Login and Sign up

The Admin app uses email and password registration, in contrast to the User app which
uses Google sign in. The main reason behind this is that using Google sign we weren’t able
to generate an actual new account (and therefore uid) in Firebase Authentication, and as it
will become evident below, the uid is an important part of the Admin app’s functionality.
Password length follow’s Google’s requirements of being between 8 to 60 characters long.
You may then proceed to login with the newly registered account.
An added functionality of the Admin app is that if you login, but don’t log out, then you stay
logged in.


#### 2.2.1 Add ID

This function, or module, consists of the previously mentioned functions – Capture Image,
Upload, NFC Transfer. Add ID is simply an activity or view with these functions (buttons)
available. There is also an ImageView in this activity, that displays the image taken, so for
example if the admin isn’t satisfied an image’s quality, he or she can take another image,
which again, will be displayed.

#### 2.2.2 Capture Image

Capturing an image is done using the device’s default camera application. Clicking on the
‘Capture Image’ button simply opens this default camera application, and after an image is
taken and confirmed, it will be shown in the ImageView.

#### 2.2.3 Upload

After the image is taken and the admin is satisfied with it, he can click on Upload, which
namely, uploads it to the cloud. For all of our cloud functionality we use Firebase, since it
provides all of the services that we need. The image is uploaded to Firebase Storage, and
each image has a corresponding URL, which we use to reference a specific image.
Once the image has been uploaded, a check happens to see whether the logged in user of
the Admin app is authorised to sign (validate) an image, or not. To be able to sign an image,
the user’s UserID (uid) has to be present in our Firebase Database (note: this is not the
same as Firebase Storage). Firebase Database is a database of documents (that can have
whatever properties/values that you assign to them), and collections of documents. This
means that no server-side code is needed, and we can add ‘real’ Admin users to the
database simply by creating a new document in the following format: key =
UNIQUE_USER_ID (this is a string). Signing an image happens by updating the image’s
metadata with a uid. Here is appropriate pseudo code to better aid the understanding of this
functionality.
```
# Signing an image
uid = get.userid()
if uid in database:
signImage()
```

#### 2.2.4 NFC Transfer

As previously mentioned, this function isn’t shown in the diagram for simplicity’s sake. This
function, or activity, transfers the URL that has been generated after the corresponding
image has been uploaded to Firebase Storage. ‘NFC Transfer’ is a button right next to the
‘Upload’ button. Pressing ‘NFC Transfer’ will transfer the URL to the User app, which then
uses it for its own functionality.


#### 2.2.5 Verify ID

Verify ID is basically the main screen of the Admin app. When verifying an ID, the User app
sends a URL to the Admin app with the ID in question. When a person using the Admin app
verifies an image (ID), it will tell them whether a valid signature is present. A valid signature
is the uid of one of the uids inside of the database mentioned above. If an invalid signature is
present, or there is no signature at all, then the Admin app user will be warned about it. Here
is some simple pseudo code attempting the better explain this verification process.
```
# Verifying an image
signature = get.imageSignature()
if signature in database:
acceptId()
else:
denyId()
```

## 3. High-Level Design

![System Context Diagram](https://i.imgur.com/66KNEtr.png)

![Admin App Class Diagram](https://i.imgur.com/o5TtZbD.png)

## 4. Problems and Resolutions

### 4.1 NFC

Our main problem with the NFC and peer to peer communication was that there was very
little documentation on how to use it. Other apps mainly used NFC to read NFC tags, but no
other apps used NFC to send messages from one phone to another. NFC is also not used
very often and getting examples on how to use it properly, or how to implement it in an
efficient way is hard.
The way we resolved this problem was just through extensive research and testing.

### 4.2 User Generated Buttons/Dynamically Generated Buttons

An unexpected problem occurred when the buttons were dynamically generated. There are
several stages of an android app. The main ones being onCreate(), onDestroy(), onStart(),
onStop(), these stages are crucial in the activity life cycle of an app. When an app is closed,
onDestroy() or onStop() is called. When the app is opened again onCreate() is called, this
basically means that the app will read the code inside of the onCreate() method and any
code outside of this method will not be generated. Dynamically generated content is created
programmatically and is not stored in the code itself. This causes a problem, because any
user generated content will immediately be deleted on restart of an app.
The only way to solve this problem is to store the user generated content in a HashMap. The
HashMap stores user generated values such as the name of the ID which will be mapped to
the Admin ID image url. The buttons can then be generated using this information.

### 4.3 Design

Another big problem that the ID Wallet app had was designing the UI. Here is some context
about the problem. There were no apps with similar functionality and objectives as ours. The
app revolved around the idea that a user could generate a button when an “ID url” arrive
from the Admin App. The Page on the user side of the app, that contained all the IDs,
displays up to six buttons. Each button containing metadata about the ID.
Designing the app was hard because there are no other apps out there that had an activity
page filled with buttons. It was simply hard to know if we should use vibrant colours or in our
case, stick to a very simplistic design. In the end, ID-Wallet will only be used for one thing,
Storing and registering IDs. It would not make sense to have a profile button for example, as
the app would only be used to store IDs. So the most necessary things were included in the
app, in our case, a logout page, an ID page and an information page. The information page
was necessary to include because there were no apps that were even remotely similar to
ID-Wallet. So the information page included a two step tutorial on how to use it.


### 4.4 Firebase Realtime Database

The main problem with Firebase realtime Database is that it’s asynchronous. Because of
that, the only way to get the HashMap was to use a DataSnapshot, listeners and an
OnDataChange listener. So, When your code executes, it might skip over some parts of the
code because now some of your code is running on a different thread.
So the basic problem that the User app was having, was when retrieving the HashMap from
the database, there was no easy way to get the HashMap. And because the function that
retrieved the HashMap ran on a thread, it meant that you would sometimes never get the
the updated HashMap from the database.
To resolve this problem you had to structure the code in such a way as to allow specific
parts of your program to run first and make sure that the thread for getting the Database
HashMap only ran once. If the onDataChangeListener was called multiple times, every time
there was a change in the database it would generate Buttons. So instead of
onDataChangeListener, you would change it to onFirstDataChangeListener.

### 4.5 Local vs Online Storage of IDs

The app was first designed to Store the ID information locally. Android studio uses
SharedPreferences to store user generated data locally. SharedPreferences is a good way
of storing user generated data, but a very insecure way as that data may be stolen from
rooted phones.
We also believed at first that it would be a good idea to store these IDs locally, just the way
you would use your physical ID. Soon after we realised that it would be extremely
advantageous to have the IDs stored under each users account. Our vision was for every
user to have an account where their IDs would be stored. The only way to store these IDs
and to be able to access them from anywhere was through the use of a database.
The biggest action we took during the design of the system was deciding on changing how
the user data was stored. It made complete sense to use Firebase realtime Database to
store User ID information and it was one of the biggest problems that we had when
implementing that part of the system.

### 4.6 Splitting The App Into Two

Originally the ID-Wallet app contained both the User and Admin apps, but in the early
development stages of the app we decided to split it into two separate apps after asking our
supervisor about it. Since the app has completely separate functionality from each other, it
only made sense to split it up into two separate apps.


### 4.7 PGP Security

Originally, we were going to use PGP for establishing a web of trust and for signing images.
However, once we started looking at how to implement such a system, it started looking less
realistic, as there was very little information on how to implement such a system, and how
exactly it works. The only realistic looking thing we found was a paid Android app whose
whole purpose was to manage all of the PGP related functions, such as storing keys,
creating keys, managing keys, and managing your web of trust.
There have also been online articles on how a serious vulnerability has been found in PGP,
called EFail, so perhaps it wouldn’t have been worth the time and effort getting PGP to work,
while in the end it’s not that secure.
Instead of using PGP, we have ended up implementing our own system, that uses
Firebase’s Realtime Database. As has already been explained in System Architecture, the
concept revolves around the uid (userID, i.e. the account ID). If the uid is in the database, it’s
recognised as a legitimate Admin account, and has the ability to sign images.

### 4.8 Differentiating Between Users and Admins

It proved challenging trying to differentiate between normal users and admins. This is an
important aspect of the Admin app, as only admins should have the authority, or ability, to
sign images. As we haven’t found any straight forward, or go-to method of solving this
problem, we implemented our own solution.
Our solution is the usage of the Realtime Database. Since it’s based on a ‘document’
system, it’s straightforward to use, and we took advantage of this. As it might be clear by this
point, in our implementation an admin is any account’s uid inside of this database. The file
structure looks in the following way:
admins -> admin_name -> { uid = account_uid }
In the above hierarchy, ‘admins’ is a collection (of admin_names), and admin_name are
documents containing a single string that is the account’s uid. This implementation had the
unexpected benefit of simple control over the admins, i.e. people that are authorised to sign
images, as you simply add a new document with a uid in order to create an admin, and in
the same way you simply delete a document to remove admin status from an account.


## 5. Installation Guide

### 5.1 Prerequisites
* 2 devices with minimum Android 4.4 (KitKat)
* Devices with NFC
* An internet connection
* Enabling installation of unknown apps (APKs)
### 5.2 Enabling APKs

To enable installation of APKs, go to your Settings. In the search bar of Settings, type in
‘install unknown apps’. This will show you a page with a list of apps, select Chrome (if this is
the browser you will use to download the app). Next scroll down to ‘Install unknown apps’,
and click on it. This will finally take you to the page where you can enable APKs. Enable
APKs by enabling ‘Allow from this source’, as shown in the screenshots.

![APKs](https://i.imgur.com/U7x2bqF.png)

### 5.3 Installing the Apps

1. Note: It’s only possible to install 1 app on one device, but regardless, two devices are
    necessary to fully test out the system. One app per device.
    Download Admin app on one device, and User app on another device from this URL:
    https://idwallet.blog/2019/03/08/id-wallet-app-download/​.
2. Once APKs are enabled, you will be allowed to install the apps.
3. When app download is finished, the installation screen should automatically pop up.
    Simply click on ‘Install’.
4. Now you’ll have to install the other app on another device.


## 6. Known Problems

#### ID-Wallet user app name is not displaying:

for some reason The app name is not displaying on the ID-Wallet user Version. We believe it
has to do something with the manifest.xml file

#### ID-Wallet admin Picture 90 degrees rotation

When taking a picture of the ID on your phone, the picture will rotate 90 degrees. So to
display the image correctly in the Image View (which is displayed through the use of glide)
you need to take a picture of the ID with your phone held landscape.

#### App OverWriting on installation.

For some reason, everytime we download ID-Wallet User version for example, and then
download the ID-Wallet Admin version, One app will overwrite the other app even though the
package names are different. We don't fully understand why this is happening, but it doesn't
affect the whole system at all. The admin app will be used by bouncer for example and the
user app will be used by users.
```
package="com.example.userid"
package="com.example.walletid"
```