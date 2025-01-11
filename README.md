# **Meal Match App**

## **Project Overview**
The Meal Match App is an Android application developed in Kotlin to facilitate collaboration between restaurants and charities. This app aims to optimize the redistribution of surplus food and resources, promoting sustainability and supporting communities in need.

## **App Icon**
![icon](https://github.com/user-attachments/assets/178f0753-34ac-447e-bc35-2658c7eb2bca)

## **App Description**  

The application enables:  

### For Restaurants: 
- **Add Donations:** Restaurants can create donation entries for surplus food or resources.  
- **Update Donations:** Restaurants can edit or manage their existing donation entries.  
- **Receive Notifications:** Restaurants are notified in real-time when a charity is coming to pick up a donation.  

### For Charities:  
- **Claim Donations:** Charities can browse and claim donations from restaurants.  
- **View Order History:** Charities have access to their past order history to track their contributions.  
- **Search for Restaurants:** Charities can search for restaurants registered on the app to find potential donation sources nearby.

## **Technologies Used**  
- **Kotlin** for Android app development.  
- **Firebase Realtime Database** for storing and retrieving structured data in real-time.  
- **Firebase Storage** for uploading and managing images and other files.  
- **Material Design Components** for a modern and intuitive user interface.

## **Screens**

## Login Screen
The MealMatch login screen is the entry point for users, providing a simple and intuitive interface for authentication.

ָָָָ**User Inputs:**

- **Name:** Enter the user's full name.
- **Email:** Provide a valid email address for account identification.
- **Password:** Securely input the password for authentication.

**User Selection:**
- **Restaurant:** For restaurants offering donations.
- **Organization:** For charities claiming donations.

**Actions:**
- **Log In:** For existing users to access their account.
- **Sign In:** For new users to create an account and join the platform.

- <img src="https://github.com/user-attachments/assets/3a9976cb-0436-4a14-8962-7fdd218fabe8" width="200" height="400">

## Sign In Screen
The registration page in MealMatch app lets users sign up by providing key details.

**User Selection:**
select user Type for sigh in:
- **Restaurant:** For creating restaurant user.
- **Organization:** For For creating organization user.

ָָָָ**User Inputs:**

**For restaurant:**
- **Restaurant Name:** Enter the user's full name.
- **Email:** Provide a valid email address for account identification.
- **Password:** Securely input the password for authentication.
- **Phone Number:** Input a contact number for communication.
- **Address:** Fill in the restaurant's physical address.
- **Restaurant Type:** Specify the type or cuisine of the restaurant.
- **Open And Close hours:** Provide the opening and closing times of the restaurant.

**For organization:**
- **Restaurant Name:** Enter the Restaurant's full name.
- **Email:** Provide a valid email address for account identification.
- **Password:** Securely input the password for authentication.
- **Phone Number:** Input a contact number for communication.
- **Address:** Input the organization's physical address.

**Instructions:**
1. The name should be between 5 and 30 characters long.
2. The password must be between 4 and 20 characters long , at least one lowercase letter , at least one uppercase letter and at least one digit.
3. The phone number must start with "05" and be followed by exactly 8 digits.
4. The address must have three parts separated by commas: Street name , Street number , City or area name.

**Actions:**
- **Sing In:**  After filling in all the fields correctly, a new user is created in the system
  
<img src="https://github.com/user-attachments/assets/9f2fd105-953b-4c11-988c-4ba8ee1a758d" width="200" height="400"> <img src="https://github.com/user-attachments/assets/79ec50d8-2b2b-4f59-b175-d9eb621e782e" width="200" height="400">  <img src="https://github.com/user-attachments/assets/82628d8d-4849-4143-92fa-5bad10a94224" width="200" height="400">

## Restaurant Home Screen
The Restaurant Home Page in the MealMatch app offers restaurant users the following options: add a new donation , update an existing donation , view notifications for pickups by charities.

**Actions:**
- **Add New Donation:** This option allows restaurants to donate food or meals to charitable organizations.
- **Update Existing Donation:** This option allows restaurant to update donation if there are any changes.
- **Remove Donation:** This option allows restaurants to remove donated food if neccesery.
- **View Notifications:** This option allows restaurant to see  notifications whenever a charity has a pickup for their donation.
- **Log Out:** Log out from the user and back to Login Activity.
  
ָָָָ**User Inputs:**

**For Add New Donation Action:**
- **Donation Name:** Enter the donation's name.
- **Donation Amount:**  Specify the quantity of the food being donated.
- **Donation Description:** Provide a brief description of the donation.
- **Donation Image:** Upload a photo of the donation, to provide charities with a visual reference.
  
**Update Existing Donation:**
- **Donation Amount:**  Specify the current quantity of the food being donated.


<img src="https://github.com/user-attachments/assets/6c1d192a-9462-4334-a428-cc20a0ae18c0" width="200" height="400"> <img src="https://github.com/user-attachments/assets/4393fea4-181a-4f5a-ab42-53601de1ba02" width="200" height="400"> <img src="https://github.com/user-attachments/assets/3ab02d33-f648-4d99-ba36-f280a14d9579" width="200" height="400">   <img src="https://github.com/user-attachments/assets/4308a35b-936c-45c7-9996-2fb6dc2f2a26" width="200" height="400">


## Organization Home Screen
restaurant home in MealMatch App Screen Contain: two fragments one for searching for a restaurant and the second for the restaurants found in the search.
**Actions:**
- **Log Out:** Log out from the user and back to Login Activity.
- **View Order History:** This option allows organization to see their order history.
- **Search For A Restaurant:** This option allows charities to search for restaurants who want to donate.
- **Select A Restaurant:** This option allows charities to select restaurant and see the donations it offers.

**User Inputs:**
**For Search For A Restaurant:**
- **Restaurant Name:** Enter the restaurant's name you looking for.


<img src="https://github.com/user-attachments/assets/503095ec-c545-4a50-be4a-bb0cc94fc9cf" width="200" height="400"> <img src="https://github.com/user-attachments/assets/c9715c1d-0975-4639-bfcf-ec017f508c17" width="200" height="400">

## Restaurant's Donation List (Navigate From Organization Home Screen)
The Restaurant's Donation List in the MealMatch app shows the donationList of a restaurant that the charity user picks.

**Actions:**
- **Log Out:** Log out from the user and back to Login Activity.
- **Previus Screen:** move to Organization Home Screen(previous Activity).
-  **Select A Donation:** This option allows charities to select donation and quantity from the restaurant the user picks.

**User Inputs:**
- **Amount:** Enter the amount of units you want from specific donation.


<img src="https://github.com/user-attachments/assets/9e29fc30-bf2b-41cd-9297-110eaaf77167" width="200" height="400"> <img src="https://github.com/user-attachments/assets/713a1f0c-977a-4d20-bc45-1878354dc329" width="200" height="400">

