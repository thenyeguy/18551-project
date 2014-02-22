18551-project
==============

An Android synthesizer designed for real time performance. Developed by Michael
Nye and Michael Ryan as a senior capstone design project.

To checkout this repository, the ClickTrack submodule must be initialized. This
can be perform by using:

    # To checkout a new copy of the repository in one step
    git clone --recursive git@github.com:thenyeguy/18551-project.git

    # To initialize in two steps
    git clone git@github.com:thenyeguy/18551-project.git
    git submodule init

To pull changes from the ClickTrack repo, perform:

    git submodule update


ClickTrack
----------

This project uses a port of a project by Michael Nye. Entitled ClickTrack, it is
also [hosted on GitHub](https://github.com/thenyeguy/ClickTrack). The project is
an audio processing framework written C++, and is used as the backend of this
project, by integrating with the Android NDK. This project's submodule tracks
the `ClickTrack/android-master` branch.

Since ClickTrack has been developed for some time, it is important for the
capstone project to track what has been developed specifically as part of the
capstone, versus what code was already written. So far, that list is:

1. Numerous code refactors
2. PolyBLEP oscillators
