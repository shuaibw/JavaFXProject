# Car Inventory Manager with JavaFX
## _Term Project for L1T2_
-------------
An inventory manager for cars with multi-threading and local networking support. This project was aimed to learn about networking and multi-threading in Java.

## Features

- Login as seller, add/remove/update cars from inventory
- Login as buyer, buy/view cars from all available sellers
- Support for multiple instances of sellers and buyers
- All information between buyers and sellers are updated synchronously

## Pre-requisites

- Download [openjfx 11.0.2](https://download2.gluonhq.com/openjfx/11.0.2/openjfx-11.0.2_windows-x64_bin-sdk.zip) and unzip contents
- Clone and open this repository in any suitable Java IDE (IntelliJ IDEA preferred)
- Add `openjfx-11.0.2/lib` to project libraries. From IntelliJ, this can be done by navigating to `Project structure -> Libraries -> +`

## Getting Started

 - `src/server/Server.java` is responsible for handling authentication requests and synchronizing data between multiple instances. Run `Server.main()` first. The login information can be found in `Server.java`. Users with `access=1` are sellers, and customers are with `access=0`.
 - `src/gui/Main.java` is responsible for the GUI interface. Run `Main.main()` and the app should be connected to server upon running. Login with the user information stored in `Server.java`.
 - Multiple instances of the GUI can be run if needed. For this, enable `Allow multiple instances` in the edit/run configuration within IntelliJ.
