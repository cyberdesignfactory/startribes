# Star Tribes

## apps/viewer

This is where the game is played.  It uses the react-pixi library to display the game world, which is stored under the :world key in re-frame's app-db.

It uses the domain/game library to run through a "game cycle" several times per second which updates the world data, which is then automatically re-rendered by re-frame / react-pixi.

It responds to user input and updates the "controls" on the player's ship (currently hardcoded to be the yellow :y-1 ship), which is then taken into account by the game cycle.

It contains a UI where the player can select a mission to attempt and displays the countdown timer, ongoing actions required and ultimately the mission's success or failure status.

## License

Copyright 2026 Robert J Symes

Distributed under the Eclipse Public License, the same as Clojure.

