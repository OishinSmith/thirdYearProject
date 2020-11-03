# Functional Specification - ID Wallet
## 0. Table of Contents
- 0. Table of Contents
- 1. Introduction
   - 1.1 Overview
   - 1.2 Glossary
- 2. General Description
   - 2.1 Product / System Functions
   - 2.2 User Characteristics and Objectives
   - 2.3 Operational Scenarios
      - 2.3.1 Customer Login
      - 2.3.2 Customer ID Registration
      - 2.3.3 Staff ID Registration
      - 2.3.4 ID Wallet Verification
   - 2.4 Constraints
      - 2.4.1 Money
      - 2.4.2 Sample Size
      - 2.4.3 NFC Bit Rate
- 3. Functional Requirements
   - 3.1 Sign Up
   - 3.2 Login
   - 3.3 Web of Trust with PGP
   - 3.4 Query Cloud Server for IDs
   - 3.5 Add ID
   - 3.6 Delete Account
   - 3.7 Metadata Transfer via NFC
   - 3.8 Delete ID
- 4. System Architecture
- 5. High-Level Design
- 6. Preliminary Schedule
- 7. Appendices
   - 7.1 References

## 1. Introduction

### 1.1 Overview
ID Wallet will be an Android application. Its aim is to reduce the need for physical
identification documents in everyday scenarios. It will attempt to achieve this by supporting
the Irish Age Cards, Passports, and national identity cards of the European Economic Area.

The need for the system is one of security and convenience. It should improve security
because you wouldn’t need to carry your physical documentation with you all of the time.
There would still be some scenarios where you would most likely need physical
identification, but they wouldn’t be every day scenarios. For example, using commercial air
travel. The app is mainly intended for use in less formal scenarios, for example entering a
nightclub. The occasions in which you could substitute physical identification with our app
would vary based on its levels of adoption.

The app would be convenient since many of the working people of Generation X, and the
majority of Millennials, along with many of the Generation Z age group, have a smartphone
in their pocket when they leave their house. For these people, using, and taking out their
smartphones, is more convenient and faster, than taking out their wallet, and then their
physical identification.

The app’s main function is to associate up to three different types of identification with a
user, and to safely store it on our cloud server. This also of course means there is a
registration and login function, since the documents are associated with an account. We will
implement different levels of permissions for the accounts. This will be necessary due to how
our system will work. As mentioned above, we will use a cloud server, and also a PGP
server to implement a Web of Trust.

### 1.2 Glossary
**Cloud Server:** A cloud server is a logical server that is built, hosted and delivered through
a cloud computing platform over the Internet.

**Metadata:** A set of data that describes and gives information about other data. For
example, author, date created and date modified and file size are examples of very basic
document metadata.

**NFC:** Near-field communication is a set of communication protocols that enable two
electronic devices, one of which is usually a portable device such as a smartphone, to
establish communication by bringing them within 4 cm of each other.

**PGP:** Pretty Good Privacy is an encryption program that provides cryptographic privacy and
authentication for data communication. PGP is used for signing, encrypting, and decrypting
texts, e-mails, files, directories, and whole disk partitions and to increase the security of
e-mail communications.

**Web of Trust:** In cryptography, a web of trust is a concept used in PGP, GnuPG, and other
OpenPGP-compatible systems to establish the authenticity of the binding between a public
key and its owner.

## 2. General Description

### 2.1 Product / System Functions
As explained in the overview, the app’s main function is to associate IDs with an account, or
user. This means that the user has to be able to register and then login to his or her account.
Here is a list of functionalities necessary for our system to work as intended, this includes
backend functionality.
- Sign Up
- Login
- Add ID
- Delete ID
- Delete Account
- Metadata Transfer via NFC
- Query Cloud Server for IDs
- Web of Trust with PGP

### 2.2 User Characteristics and Objectives
The main objective of the app is to provide an alternative to physical IDs. The requirements
of the system from the users perspective is that is cannot be complicated to use. So we are
trying to make it as simple as it sounds. The customer creates an account, registers their ID
on the account through a member of staff and from that point onward, they may use NFC to
show the member of staff their ID.
Users are expected to know how to use a smartphone and have to know how to register for
an account. Users will also have to understand how NFC works and how the first verification
of the ID is done. There will be a written tutorial shown on the app’s GUI.
Things we wish to include in the app:
- A tutorial which will show users how to use the app
- Store multiple IDs
- Store various types of IDs
- The app will also have a simple/user-friendly UI
- The system has to be user friendly
- The registering process should be simple


### 2.3 Operational Scenarios

#### 2.3.1 Customer Login
A customer has to register for an account on ID Wallet for them to have access to the
functionality that the system provides. They have to put in an email address and a password.
If the email or password is not valid, the customer will be notified.

#### 2.3.2 Customer ID Registration
When the customer interacts for the first time with a bouncer (staff) with the purpose of
having their ID verified and available on the app, the customer must first show the bouncer
their physical ID, and if the bouncer deems it valid, only then, will the bouncer choose to
register the ID on the app.

#### 2.3.3 Staff ID Registration
After the Staff (Bouncer in this case) member validates the physical ID of the customer, the
staff member will then take a picture of the ID. The ID Wallet account will be notified that the
ID has been validated and accepted.

#### 2.3.4 ID Wallet Verification
Now that the staff member has validated the ID and the customer is able to use their virtual
ID, from this point onwards, the staff and customer will interact through NFC. The customer
will take out their phone when asked to show ID, the customer will then open up the app and
enable NFC and bring their phone close to the staff members phone. The app will then
retrieve the ID photo from the database to check if the ID belongs to the customer.

### 2.4 Constraints

#### 2.4.1 Money
We wanted to utilize dedicated server hosting, which is provided by many different
companies, but the majority of the starting packs begin around the 200 EUR mark per
month. This kind of price range is out of our reach. The only option within our reach is to use
Virtual Private Server hosting. These services are available for even as low as 10 EUR per
month, although we haven’t research their quality of service yet.

#### 2.4.2 Sample Size
As we want to use the Web of Trust concept provided by PGP, we will need to be able to
test this infrastructure. We should be able to do so for a small number of users, as we can
simply mimic a small system ourselves, but it might very likely prove challenging preparing a
larger number of users for testing. Since we cannot have actual users test this, we might
have to create some virtual users and code them the basic functionalities needed to test
such an infrastructure.

#### 2.4.3 NFC Bit Rate
We will need to minimize the size of the data that gets transferred between the two
communicating devices. This is because NFC’s maximum supported bit rate is 424 kbit/s
(Kilobits/s), that’s 0.424 Mb/s (Megabits/s), or 0.053 MB/s (Megabytes/s). To give more
perspective to these speeds, we will use an example.
Here is a table showing an equal storage size in different units.
Kilobits (kbit) Megabits (Mb) Megabytes (MB)

|                 | Kilobits (kbit) | Megabits (Mb)  | Megabytes (MB) |
| --------------- |---------------- |--------------- |--------------- |
| Size            | 4000            | 4              | 0.5            |

With a 424 kbit/s bit rate, the above size of 0.5 MB, would take 9.44 seconds to transfer. It’s
obvious from these numbers that transferring files above a certain size becomes unrealistic.
Therefore we need to minimize the data size to be transferred, so that we don’t
inconvenience our users.


## 3. Functional Requirements
The following list shows the functional requirements in ranked order.

1. Sign Up
2. Login
3. Web of Trust with PGP
4. Query Cloud Server for IDs
5. Add ID
6. Delete Account
7. Metadata Transfer via NFC
8. Delete ID

### 3.1 Sign Up
**Description**\
Sign up is a basic function. The individual simply enters their email address and their desired
password. The email address and password provided will be used for logging in once they
have completed the sign up process.\
**Criticality**\
Signing up is at the top of the list because without it, it wouldn’t be possible to implement our
system. Since we need a way to associate data (an image of an ID document) with a specific
user, an account, and therefore sign up, becomes necessary.\
**Technical Issues**\
We will need to ensure the passwords entered follow certain criteria for safety reasons,
otherwise they will have to be rejected. We will also need to check whether a valid email
address has been entered, if not, then the process fails.\
**Dependencies**\
None.

### 3.2 Login
**Description**\
Signing up on its own is useless without being able to login. Login allows the use to access
their newly created account. The login screen will simply require the user to enter their email
address and password which they have used for signing up.\
**Criticality**\
The login function is of utmost importance, since, as mentioned previously, without being
able to associate accounts with data, our system would be impossible to implement.\
**Technical Issues**\
With the login function there are concerns regarding security. We need to make sure the
entered details are properly encrypted and secured when communicating with our app and
server.\
**Dependencies**\
The login function is dependant on the previous functional requirement, i.e. Sign Up.

### 3.3 Web of Trust with PGP
**Description**\
The web of trust is necessary to establish the authenticity of the binding between a public
key and its owner. There are two keys pertaining to a person: a public key which is shared
openly and a private key that is withheld by the owner. The owner's private key will decrypt
any information encrypted with its public key. Normally in the web of trust, each user has a
ring with a group of people's public keys, this is true in our implementation as well, but, the
users of our app, will not be able to sign and verify signatures.\
**Criticality**\
This is an important part of our system because it helps manage our desired hierarchy of
managers and employees below them (e.g. bouncers). It allows us to see who signed how
many signatures, and who these signatures belong to, giving us enough information to
manage and oversee the system. PGP is the encryption program used, and the web of trust
is simply the infrastructure.\
**Technical Issues**\
Numerous issues may arise. We have no experience with running a PGP key server and
managing it. We also might lack the number of people necessary to create a big enough web
of trust, to see how it develops, and any unforeseen issues that might arise from running
such a server.\
**Dependencies**\
The web of trust and PGP is under one section, but it’s fair to say the web of trust is
dependant on PGP, since without it the cryptography element would be missing, leaving
simply an idea.

### 3.4 Query Cloud Server for IDs
**Description**\
This will allow us to access the users’ IDs from our cloud server. These IDs are stored on the
server in the form of images. These IDs will be displayed on the bouncer’s app when he
pairs his device with the user’s device.\
**Criticality**\
This is highly critical since without it the user’s ID would not be shown, making our app fail its
primary function (of attempting to reduce the number of scenarios where physical ID is
necessary).\
**Technical Issues**\
We have limited knowledge working with databases so this might prove challenging. We also
don’t have experience using a cloud server, especially for such a task. Some issues we
except that might occur are; incorrect data placed in the database, and the data itself might
become corrupt for some unforeseen reason.\
**Dependencies**\
None.

### 3.5 Add ID
**Description**\
This will allow the bouncer to add an ID to a specific user. This will happen the following
way: the bouncer takes an image of the user’s physical ID, and then this image gets
uploaded to our cloud server. A secondary function of Add ID, or a consequence of it, is the
generation of a unique piece of data, and hiding this in the header of the ID image. This will
be used for extra security later on.\
**Criticality**\
This is critical since it’s the core of the idea. These virtual IDs are supposed to help the
users.\
**Technical Issues**\
We might end up damaging the original header if we’re not careful with our implementation
of adding this extra, hidden metadata.\
**Dependencies**\
This functional requirement is dependant on Sign Up and Login.

### 3.6 Delete Account
**Description**\
This will allow the user to permanently delete their account, along with any IDs stored in our
cloud server. In the settings menu, there will be an option to delete the user’s account.\
**Criticality**\
This function is not particularly important. It’s more of a peace of mind for the user, knowing
they can permanently delete their information.\
**Technical Issues**\
We will need to make sure the correct account will be deleted, and that their corresponding
IDs will be removed from our database.\
**Dependencies**\
This function is dependant on Sign Up and Login. You need an account in order to delete it,
but you also need to be logged in.

### 3.7 Metadata Transfer via NFC
**Description**\
The purpose of this function will be to transfer a portion of the ID image’s metadata back to
the user. This will be used as an extra security step. Since when the bouncer takes an
image, and no problems have arisen, a unique piece of metadata will be generated and
added to the ID image’s header. This unique data is what’s going to be sent back to the user
via NFC. The user will then store this metadata for later use, when he or she will want to use
the ID.\
**Criticality**\
This functional requirement is of a lesser importance because it’s not necessary to make our
system function properly. It’s simply an extra security layer. When the user wants to use his
or her virtual ID, there will be a check to see whether the metadata from the image (that’s on
our cloud server), matches that of the user (requested via NFC).\
**Technical Issues**\
There are Java libraries for NFC and metadata, but we haven’t used them. So, it’s very likely
we’ll run into some unforeseen issues.\
**Dependencies**\
This functional requirement depends on Add ID, and all of its dependencies.

### 3.8 Delete ID
**Description**\
This will allow the user to delete a specific ID. Deleting it will permanently remove it from our
cloud server. The user will be warned of this when they press the delete button.\
**Criticality**\
This functional requirement has little importance, but as with the Delete Account function, it
gives the user a peace of mind that we won’t own their data forever. It’s also likely that users
will either delete their IDs, and or accounts when they don’t need it anymore, essentially
keeping our database a little bit cleaner of useless data.\
**Technical Issues**\
There shouldn’t be any. We only have to make sure that the deleting process deletes the
correct ID every time. This can be easily checked with a few test cases.\
**Dependencies**\
An ID is needed in order to delete it, this means Add ID, and all of its dependencies are
necessary.

## 4. System Architecture
![alt text1](https://i.imgur.com/Gqoi2kc.png)

**Admin/Staff**\
The Admin/Staff will be interacting with the User. Staff will verify the User’s/Customer’s ID
and will then choose to take a picture of the ID using the app. The image of the ID will then
be stored in the Database/Cloud.

**User/Customer**\
User/Customer will interact with the Admin/Staff. The first time ID Wallet is used, the User
shows Staff their ID. From then onwards, the Staff will scan the app on the User/Customer
phone which will send metadata about the ID image’s unique, hidden data. This metadata
will be used to add an extra security layer to our app.

**App**\
The app will have two functions. One function will be used by the User, while the other
function will be strictly for use by the Staff/Managers of the location.

**Database/Cloud**\
The Database/Cloud will store the images of the IDs of the User/Customer.

**Key Server**\
The Key Server will be used by the Manager(s)/Staff of the location. There will have to be a
system such as PGP in place as a way to trust that signatures are authentic.

## 5. High-Level Design
![alt text1](https://i.imgur.com/NC2MBtJ.png)

This diagram explains how the PGP key server and PGP works. This specifically
demonstrates how the data is encrypted.Data is generated by the Manager(s) and a random
key is also generated. The Data generated by the Manager is encrypted using the randomly
generated key. And the Randomly generated key is then encrypted using the public staff key
that is requested from the PGP key server (The PGP server contains the public keys that
each Staff/Manager sends to it). Both the encrypted Data and key is sent to the
Staff/Manager.

![alt text1](https://i.imgur.com/1WvjL1e.png)

This diagram shows the decryption process of the data sent from the manager(s). The
encrypted key is decrypted by the staff’s/manager’s private key, in turn you get the original
randomly generated key (generated by the manager) that is used to decrypt the data.

![alt text1](https://i.imgur.com/YbA0JbY.png)

This Context Diagram explains how ID Wallet (the System) works. The diagram shows how
the different external entities interact with each other and the Process.

## 6. Preliminary Schedule
We used a Gantt chart to provide an initial version of the project timeline plan. The project
start and stop dates dates were important and fixed. We had a total of around 39 days to
complete the project. We made the Gantt chart using Microsoft Excel.
Hardware: Smartphone, Computer, Laptop
Software: Cloud Server, PGP Key Server, Android Studio, Windows, MySQL,

**Gantt Chart**
![alt text1](https://i.imgur.com/da8GClv.png)

**Gantt Chart Data in Microsoft Excel**
![alt text1](https://i.imgur.com/UViapSH.jpg)

## 7. Appendices

### 7.1 References
- http://www.google.com
- http://www.wikipedia.com
- http://www.youtube.com
- http://www.quora.com
