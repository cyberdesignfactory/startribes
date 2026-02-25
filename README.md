# Star Tribes


Star Tribes is a space combat game, written in Clojure.

Lead your tribe to glory by completing a series of missions...

<img src="https://www.cyberdesignfactory.com/img/startribes.png" xxalign="right" width="360" />

## Live Demo

https://startribes.cyberdesignfactory.com

## Installation / Running Locally

Ensure the following are installed:
- Java
- Leiningen
- node/npm/npx

Steps:
- git clone https://github.com/cyberdesignfactory/startribes
- cd startribes
- cd apps/components
- lein install
- cd ../viewer
- npm i
- npx shadow-cljs watch app

The webapp should now be running at http://localhost:8280

## Codebase

apps/viewer:

This is where the game is played.  It uses the react-pixi library to display the game world, which is stored under the :world key in re-frame's app-db.

It uses the domain/game library to run through a "game cycle" several times per second which updates the world data, which is then automatically re-rendered by re-frame / react-pixi.

It responds to user input and updates the "controls" on the player's ship (currently hardcoded to be the yellow :y-1 ship), which is then taken into account by the game cycle.

It contains a UI where the player can select a mission to attempt and displays the countdown timer, ongoing actions required and ultimately the mission's success or failure status.

apps/previewer:

This is used to isolate each graphical component for ease of development.

apps/components:

This contains mainly graphical (react-pixi) components which can be used across the client apps to render the world, or individual ships etc, graphically.

domain/game:

This contains the game logic.  Several discrete unit-tests features reside here, which are brought together in a "cycle".  Each feature (and the overall game cycle) transforms a "world" data structure into a new data structure.

domain/projection:

This essentially deals with the various obstacles in the game - it adjusts the positions of the ships to take account of the obstacles.

domains/worlds:

This contains various initial world setups that are used by the various missions.

It also contains various helpers for constructing a world.

## TODO

- Improve missions and worlds, perhaps several 'campaigns' each with a set of missions

## Rationale

TODO

## License

Copyright 2026 Robert J Symes

Distributed under the Eclipse Public License, the same as Clojure.

