# symmetric-android-client-demo

This project is a sample SymmetricDS android client that is configured as a store node for the quick start/demo tutorials located here: https://www.symmetricds.org/doc/3.9/html/tutorials.html

This android application will create an embedded SQLite database, all SymmetricDS run-time tables, and configure the quickstart/demo tables (ITEM, ITEM_SELLING_PRICE, SALE_TRANSACTION, SALE_RETURN_LINE_ITEM) for synchronization.  The application has a basic user interface that can run queries on the SQLite database to demonstrate synchronization with a quickstart/demo corp server.

# Build Instructions

This project was developed using Android Studio 3.0.1, Android SDK 26, and Java 8

To compile, the SymmetricDS .jar files must be built from the SymmetricDS source code and copied into the android project.

To generate the libs directory, clone the symmetric-ds GitHub repository and run the following command in the symmetric-assemble directory: 
./gradlew androidDistZip

The libs directory will be outputted in a .zip archive to the symmetric-android/build/distributions/

Copy the generated libs directory into the app/ directory of the android project and build the android project

# Please Note

In order to sync properly, the Sync URL of the corp-000 node must be updated to use the IP address of host rather than localhost.  

Then, update the String REGISTRATION_URL in the DbProvider class of the android project to the new Sync URL of the corp-000 node. 


