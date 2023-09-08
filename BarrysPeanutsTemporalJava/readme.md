# Implementing the flattened style of the Actor Model under Temporal

This project demonstrates executing a Temporal Workflow behavior using signals only. Taking a "signal only"
approach supports the principle of the Actor Model that states that communication between actors is facilitated via
messaging only. In Temporal a message is called a signal. Temporal supports signaling out of the box.

Intrinsic to the Temporal framework are the Temporal Server and Worker.

You can think of the Temporal Server as a repository for storing information about the state of the workflow
as well as being the message broker that manages messages relevant to the given workflow’s operation.
The Worker is the component that receives messages and then executes behavior defined in the workflow according to the
message received.

Applications, by way of another Temporal component called a Client send signals relevant to the particular Temporal workflow to the Temporal Server. (Under
Temporal, a message is called a signal.) The Temporal Worker listens on a task queue hosted by the Temporal Server for signals relevant to the given workflow.
The Worker receives a signal from the task queue and then forwards it on to the Workflow code. The Temporal workflow responds to the signal received. (See Figure 1.)

| ![Temporal Architecture](./images/temp-arch-01.jpg)                                 |
|-------------------------------------------------------------------------------------|
| Figure 1: Adopting the Akka messaging style of communication to a Temporal Workflow |

# Running the code:

The [Java Virtual Machine](https://openjdk.org/) and [Maven](https://maven.apache.org/install.html) need to be installed
on the host computer.

## (1) Confirm that Java and Maven are installed on the host machine

Confirm that Java is installed:

```bash
java --version
```

You'll get output similar to the following:

```bash
openjdk 18.0.2-ea 2022-07-19
OpenJDK Runtime Environment (build 18.0.2-ea+9-Ubuntu-222.04)
OpenJDK 64-Bit Server VM (build 18.0.2-ea+9-Ubuntu-222.04, mixed mode, sharing)
```

Confirm that Maven is installed:

```bash
mvn --version
```

```bash
Maven home: /usr/share/maven
Java version: 18.0.2, vendor: Oracle Corporation, runtime: /usr/lib/jvm/jdk-18.0.2
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.19.0-46-generic", arch: "amd64", family: "unix"
```

## (2) Download and install the Temporal CLI (which includes the server)

If you do not have the Temporal server installed, click the link below to go to the Temporal documentation that has the
instructions for installing the Temporal CLI.

[https://docs.temporal.io/cli/#installation](https://docs.temporal.io/cli/#installation)

The Temporal development server ships with the CLI.

---

## (3) Start the Temporal Server

Here is the command for starting the Temporal Server on a local Ubuntu machine. Execute the command in a terminal
window.

```bash
temporal server start-dev
```

---

## (4) Do some maven housecleaning

Run the following command in a new terminal window to create a fresh Maven environment:

```bash
mvn clean package install
```

## (5) Start the application

In that same terminal window run:

```bash
mvn exec:java -Dexec.mainClass="barryspeanuts.App"
```

