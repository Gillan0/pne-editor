# Petri Net Editor

## About the Project

This project is a Petri Net Editor designed to model and analyze the behavior of discrete-event systems, such as computing or industrial processes. A Petri net is a mathematical model consisting of: \
<ul>
<li>Places: Represent states and hold tokens.</li>
<li>Transitions: Represent events or changes.</li>
<li>Arcs: Connect places and transitions with a defined value (default: 1).</li>
</ul>

## How it Works
A transition is enabled when its input places have enough tokens to satisfy the incoming arc values.
When a transition fires, tokens are transferred from input places to output places based on the arc values.
The simulation allows dynamic changes to token distribution, enabling the modeling of complex system behaviors, with a visual interface.\

This simulation uses a formerly developped user interface. Our implementation Simply adapts our Petri Net Simulation project to be used in this wrapper.\

Java Implementation: Developed in Java (version 23.0.1) with JUnit 5 for model testing (model tests not included).


# Getting Started
##Installation
Clone the repository:\
```
git clone https://github.com/Gillan0/pne-editor.git
```
Open the project in your preferred IDE.

## Running the Simulation

Navigate to the src/org/petrinet/editor/Main.java class \
Run the class and you will have access to the user interface to build and run your own Petri net !

# Development

This project was developed by Antonino GILLARD and Othmane EL AKRABA as part of a university assignment. The source code is managed via Github.


# Technologies Used:
Java Development Kit (JDK): Ensure you have JDK 23.0.1 or higher installed.\

# Contribution
This project is closed for external contributions, but feel free to fork the repository for personal use. If you'd like to provide feedback, contact the developers directly.

# License

This project is licenced under the GNU GPLv3. \
Except for our model and the adapters developped, all credit goes to Martin Riesz <riesz.martin at gmail.com>