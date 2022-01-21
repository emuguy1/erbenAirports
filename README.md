# ErbenAirports

Emanuel Erben
Matr.Nr. 3174827

Main focus is to write a spring java project that uses at least one service from another student and also publishes one
service.

Main settings are saved in application.properties.

## Sources

All graphics used are svg's from open source project undraw:
<a>https://undraw.co/illustrations

As Dokumentation and tutorial for the project, <a>https://www.baeldung.com/ was used.

## Explanations

Flight bookment only works in combination with running TR-Bank on port 8934<br>
But if you log in as employee you can creat one, without using the transaction service if you leave empty created for

It was decided to not use embeddable, as there was no reason to use it somewhere.

As an Factory Method the setup process was selected, where the Factory decides between Customer and Employee Bean

## Testusers

For testing purpose you can use for an employee:<br>
username: root<br>
password: 123passwort123

For testing purpose you can use for an customer:<br>
username: root2<br>
password: 123passwort123

or you can create one, but you have to take care, that the IBAN you insert is one, that is available at TR-Bank:
here is one you can use: <br>
DE12345678901234500006


