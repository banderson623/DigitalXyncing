#DigitalXyncing

Near real time document syncing

## Collaborative Music

We would like to build a real-time, multi-person, multi-instrument collaborative band. The idea is that anyone can connect to our server, select an instrument and play along.

This would be accomplished by building a multi-threaded server that would emit all the tones (sounds and notes) and coordinate all of the clients (instruments) timing and availability.

The clients would connect to the server, and request an instrument's note to be played. The client can do this by providing information about the tone to play real time. Or it can transmit a sequence (musical-bar) of tones to be played (we would loop over the bars until a new sequence is sent). There is a lot of flexibility in how accomplish this.

Network latency may dictate a certain approach. Specifically, synchronizing the server and clients over the (ISU) wireless network may prevent real time "playing."

We even have a witty name: Digital Xymphony

We would build our own protocol to run over sockets. It may have a web client through Web-sockets. It may have an Android or iOS client as well.
