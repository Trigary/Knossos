# Knossos

A dungeon/maze generator designed with customizability and expandability in mind.
The current version is an early prototype.

## Structure, workflow

The creation process consists of two separate tasks:
the planning and the and the instantiation.
The structure of the dungeon/maze is specified in the planning stage
(where the rooms, corridors, walls, etc. should be located)
and the population of these areas (how they should look, what they should contain)
and their instantiation into the real world is done in the building stage.
In order not to create lag spikes, the instantiation can be done is smaller segments:
only a configurable amount of cells of the dungeon/maze are built each tick.

## Showcase

![image](https://i.imgur.com/u8lXbHs.png)