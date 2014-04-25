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

    # From the root of 18551-project
    git submodule update

By default, the submodule is checked out into a detached head. To modify the
repository, it is simpler if you set it to track a local branch as follows:

    # From the root of the submodule (Synth/ClickTrack/jni)
    git checkout -b android-master
    git branch -u origin/android-master


ClickTrack
----------

This project uses a port of a project by Michael Nye. Entitled ClickTrack, it is
also [hosted on GitHub](https://github.com/thenyeguy/ClickTrack). The project is
an audio processing framework written C++, and is used as the backend of this
project, by integrating with the Android NDK. This project's submodule tracks
the `ClickTrack/android-master` branch.

To build the code, you must manually run `ndk-build` in the
`Synth/ClickTrack/jni` directory. The Android Studio project will automatically
include the library files.

Since ClickTrack has been developed for some time, it is important for the
capstone project to track what has been developed specifically as part of the
capstone, versus what code was already written. So far, that list is:

1. Numerous code refactors
2. PolyBLEP oscillators
3. Moorer reverberator
4. First and second order filters
5. Drum machine
6. LFOs
7. Limiter, compressor, and noise gate
8. FM synthesizer
9. Ring modulator
