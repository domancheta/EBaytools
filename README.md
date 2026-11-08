# EBaytools project


## Introduction
This application allows you to browse items in your EBay account.

Just enter the item number your interested in watching in the search box and it will let you view the product selling progress (most applicable to bidding items).



This is kind of a reboot of the [eBay-tools](https://sourceforge.net/projects/ebaytools/) project which I worked on in the '00s.

The major difference is that was written on Perl and meant to run only Windows platforms.  This is a pure Java implementation using JavaFX as the UI framework.

## Running in IntelliJ
It can be run using Maven with the existing pom.xml:
On IntelliJ right side tabs, click the Maven ('m') button
In the window popout, first compile the project
I.e.  Navigate to Lifecycle -> clean, then Lifecycle -> compile

After any dependencies are pulled, and the code is compiled, navigate to Plugins -> javafx:run

The application UI should open at this point