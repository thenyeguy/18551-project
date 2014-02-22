18551-project
==============

An Android synthesizer designed for real time performance. Develoepd by Michael
Nye and Michael Ryan as a senior capstone design project.

ClickTrack
----------

This project uses a port of a project by Michael Nye. Entitled ClickTrack, it is
also [hosted on GitHub](https://github.com/thenyeguy/ClickTrack). The project is
an audio processing framework written C++, and is used as the backend of this
project, by integrating with the Android NDK.

It is included as a submodule in this project. The submodule tracks the
`ClickTrack/android-master` branch.

Since ClickTrack has been developed for some time, it is important for the
capstone project to track what has been developed specifically as part of the
capstone, versus what code was already written. So far, that list is:

1. Numerous code refactors
2. PolyBLEP oscillators
