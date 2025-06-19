# SmartI18N

SmartI18N will die Internationalisierung von Webprojekten vereinfachen und die Umsetzung beschleunigen. Es gibt einen Online Editor mit welchem Texte in Echtzeit angepasst und verändert werden können. Für die Einbindung stehen verschiedene SDK's zur Verfügung die das Arbeiten mit SmartI18N vereinfachen. 

Mehr Informationen zu SmartI18N findest du unter [www.smarti18n.com](https://www.smarti18n.com/)

## 🚨 This project is now officially retired! 🧓🪦

Development has ended, support has vanished, and the code is now living its best life in a quiet repo somewhere, sipping digital margaritas. 🍹

Feel free to fork it, remix it, or just stare at it nostalgically — but don't expect it to do any new tricks. It’s not dead… it’s just resting. 😴

## First Steps

Für den Start empfehlen wir dir, den SmartI18N Server von [www.smarti18n.com](https://www.smarti18n.com/) zu nutzen. Später kannst du mit Hilfe eines Docker Image SmartI18N selbst hosten. Dazu aber später mehr.

Erstell dir mit Hilfe des [Editors](https://editor.smarti18n.com/) einen Account und erstell anschließend ein Projekt. Mithilfe des Projekt Keys und Secrets kannst du dann SmartI18N in dein Projekt einbinden.

## SDK's

Derzeit gibt es für Spring Message Sources und AngularJS Schnittstellen. In Zukunft wollen wir weiter Schittstellen ergänzen.

### Spring Framework SDK

Example folgt

### AngularJS SDK

Example folgt

## Docker Image

SmartI18N kann selbst betrieben werden. Mehr Informationen findest du im [Docker HUB](https://hub.docker.com/r/smarti18n/).

### MongoDB (optional)

Für den Betrieb von SmartI18N benötigst du eine MongoDB. Du kannst eine extern gehostete Instance verwenden oder einen [Docker Container](https://hub.docker.com/_/mongo/). 

```
docker run -d --name smarti18n-mongo mongo
```

### smarti18n-messages

```
docker run -d --name smarti18n-messages --link smarti18n-mongo:mongo -p 30001:8080 -e MONGODB_URL=mongodb://mongo/smarti18n-messages  smarti18n/messages
```

### smarti18n-editor

```
docker run -d --name smarti18n-editor -p 30002:8080 -e "SMARTI18N_MESSAGES_HOST=http://localhost:30001" smarti18n/editor
```

### First Login

Jetzt kannst du das initiale Admin Passwort aus dem smarti18n-messages Container suchen. 

```
docker logs smarti18n-messages
```

```
#######################################################################
Initializing Application
Opened connection \[connectionId{localValue:2, serverValue:2}\] to mongo:27017
Create Default User \[default@smarti18n.com\] with Password \[PASSWORD\]
create default project \[default\] with secret \[SECRET\]
Initializing Application finished
#######################################################################
```

Mit dem Passwort und der E-Mail default@smarti18n.com kannst du dich im Editor unter http://localhost:30002 einloggen.

## License

SmartI18n is released under version 2.0 of the Apache License.
