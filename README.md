# Rocket Launcher

A playground app for experimenting with clean architecture principles.  

## Getting Started

This project is devided into 3 main modules (layers):
1. Domain layer - All business logic and entities represented by 'use cases' and abstraction (repositories interfaces etc..)
2. Data layer - Implementation of the Domain's abstraction. Room DB for local storage, Retrofit for calling open source API.
3. Presentation layer - All UI related elements.
Another App module exist for wrapping everything together, this way I keeping a good separation of concern between layers. Most importantly no business logic in the UI layer!

### Installing

Download this repository as Zip or checkout the source code.
A showcase app is available on Google Play at:
* [Dropwizard](https://play.google.com/store/apps/details?id=ronybrosh.rocketlauncher.app) - Free download

## License

This project is completely free.
Use it as a guide for your projects or go through the code while learning clean architecture.

## Acknowledgments

To complete this project I've read many Medium blogs, Android developers blog and books:
* [Dropwizard](https://www.amazon.co.uk/Kotlin-Action-Dmitry-Jemerov/dp/1617293296/ref=sr_1_1?keywords=kotlin+in+action&qid=1564334367&s=books&sr=1-1) - Kotlin 
* [Dropwizard](https://www.amazon.co.uk/Clean-Architecture-Craftsmans-Software-Structure/dp/0134494164) - Clean architecture
* [Dropwizard](https://www.amazon.co.uk/Learning-RxJava-Concurrent-responsive-applications/dp/1787120422/ref=sr_1_2?keywords=rxjava+book&qid=1564334222&s=books&sr=1-2) - Rx Java
