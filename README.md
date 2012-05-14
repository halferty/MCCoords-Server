MCCoords-Server
===============

A java server to track player coordinates in Minecraft.

Protocol
--------

Send `<password>|<name>|<x>|<y>|<z>|<server address>`, and the server will send back a list of other players, separated by commas. The format for each player returned is `<name>|<x>|<y>|<z>|<server address>` and then a comma.

