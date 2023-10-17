# si4n6r

A [NIP-46](https://github.com/nostr-protocol/nips/blob/master/46.md) compliant signer

This is a signer that implements the NIP-46 standard.
It is meant to be used as a remote signer for
the [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) app, or any other NIP-46 compliant
app of your
choice.
Right now it is a very simple signer, a proof of concept, that only supports the `connect` (signer and app
initiated), `disconnect`, and `describe` methods.

The software is composed of four parts:

- The [si4n6r-lib](https://github.com/tcheeric/si4n6r-lib/tree/1.0-SNAPSHOT) library that implements the NIP-46
  standard.
- The [si4n6r](https://github.com/tcheeric/si4n6r/tree/1.0-SNAPSHOT) binary that implements the NIP-46 standard and
  exposes the signer as a websocket API.
- The [shibboleth](https://github.com/tcheeric/shibboleth/tree/1.0-SNAPSHOT) library that implements the NIP-46 app
  standard to connect to a signer through a websocket API.
- The [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) binary, or any other NIP-46
  compliant app of your choice, that uses the [Shibboleth]((https://github.com/tcheeric/shibboleth/tree/1.0-SNAPSHOT)
  library to connect to a signer through a websocket API.

*The app and the signer are expected to run on different Virtual Machines.*

The software is written in Java and relies on the [nostr-java](https://github.com/tcheeric/nostr-java/tree/0.3-SNAPSHOT)
library of the nostr protocol. It is currently in pre-alpha stage, it's still _untested_, and is expected to change
significantly in the near future.
It implements:

1.
A [Signer](https://github.com/tcheeric/si4n6r-lib/blob/1.0-SNAPSHOT/si4n6r-signer/src/main/java/nostr/si4n6r/signer/Signer.java)
and
an [Application](https://github.com/tcheeric/shibboleth/blob/1.0-SNAPSHOT/src/main/java/nostr/si4n6r/shibboleth/Application.java)
classes, with their own identities (public and private key pairs) for communication.
2.
A [SignerService](https://github.com/tcheeric/si4n6r-lib/blob/1.0-SNAPSHOT/si4n6r-signer/src/main/java/nostr/si4n6r/signer/SignerService.java)
to submit the connect request to the signer, handle the incoming requests from the app, and send the responses back to
the app.
3.
An [ApplicationService](https://github.com/tcheeric/shibboleth/blob/1.0-SNAPSHOT/src/main/java/nostr/si4n6r/shibboleth/AppService.java)
to submit requests to the signer and handle the incoming responses from the signer.
4.
A [SignerCommandHandler](https://github.com/tcheeric/si4n6r-lib/blob/1.0-SNAPSHOT/si4n6r-signer/src/main/java/nostr/si4n6r/signer/provider/SignerCommandHandler.java)
and
an [ApplicationCommandHandler](https://github.com/tcheeric/shibboleth/blob/1.0-SNAPSHOT/src/main/java/nostr/si4n6r/shibboleth/provider/AppCommandHandler.java)
to handle the incoming events from the app and the signer respectively.

## Workflow

1. A user connects to the signer through an app
2. The app sends a `connect` request to the signer
3. A session between the app and the signer is created
4. The signer replies with an `ACK` response message, and passes the session id to use for future requests.
5. The app can now send any other request to the signer, such as `disconnect`, or `describe` (for now, more to come in
   the future).

## Configuration

### Relay

You can configure the communication relay in the `relays.properties` file in your `$HOME/.nostr-java` folder, on both
client and server.

```
nostr_rs_relay=localhost:8080
#relay_untethr=localhost:9090
#relay_badgr=relay.badgr.space
```

### Signer identity

You configure the signer identity in the `signer.properties` file in your `$HOME/.nostr-java/identities` folder, on the
server. Here, it is assumed that your signer holds the identity with name `signer`

```
privateKey=519672a62831011....5b3998a1d65366aff97988
```

(The keys are in hex format)

## Installation and Usage

1. Build the [nostr-java](https://github.com/tcheeric/nostr-java/tree/0.3-SNAPSHOT) project. I am currently using the
   0.3-SNAPSHOT branch of [nostr-java]
2. Build the [si4n6r-lib](https://github.com/tcheeric/si4n6r-lib/tree/1.0-SNAPSHOT) project
3. Build the [si4n6r](https://github.com/tcheeric/si4n6r/tree/1.0-SNAPSHOT) project
4. Build the [shibboleth](https://github.com/tcheeric/shibboleth/tree/1.0-SNAPSHOT) project
5. Build the [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) project
6. Run the [si4n6r](https://github.com/tcheeric/si4n6r/tree/1.0-SNAPSHOT) binary on your server
7. Run the [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) binary on your client

### Sample output

```shell
nostr@localhost:~/Apps/si4n6r$ /usr/lib/jvm/jdk-20/bin/java -jar si4n6r-1.0-SNAPSHOT.jar 
Oct 17, 2023 1:17:45 AM nostr.util.ApplicationConfiguration <init>
INFO: Loading the application configuration file...
Oct 17, 2023 1:17:45 AM nostr.util.AbstractBaseConfiguration load
INFO: Loading configuration file /relays.properties...
Oct 17, 2023 1:17:45 AM nostr.client.Client getInstance
INFO: Waiting for relays' connections to open...
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Oct 17, 2023 1:17:46 AM nostr.ws.ClientListenerEndPoint onConnect
INFO: Connected to relay relay.badgr.space/74.208.24.224:443
Oct 17, 2023 1:17:46 AM nostr.ws.Connection connect
INFO: The session is now open to relay.badgr.space
Oct 17, 2023 1:17:50 AM nostr.util.ApplicationConfiguration <init>
INFO: Loading the application configuration file...
Oct 17, 2023 1:17:50 AM nostr.util.AbstractBaseConfiguration load
INFO: Loading configuration file /identities/signer.properties...
Oct 17, 2023 1:17:51 AM nostr.si4n6r.server.Daemon main
INFO: Starting the si4n6r daemon service...
Oct 17, 2023 1:17:51 AM nostr.si4n6r.server.Daemon lambda$filter$1
INFO: Creating subscription for kind-24133 events...
Oct 17, 2023 1:17:51 AM nostr.client.Client send
INFO: Sending message ReqMessage(super=BaseMessage(command=REQ), subscriptionId=si4n6r-pool-2-thread-1, filters=Filters(events=null, authors=null, kinds=BaseList(list=[24133]), referencedEvents=null, referencePubKeys=null, since=null, until=null, limit=null, genericTagQueryList=null))
Oct 17, 2023 1:17:51 AM nostr.ws.ClientListenerEndPoint onConnect
INFO: Connected to relay relay.badgr.space/74.208.24.224:443
Oct 17, 2023 1:17:51 AM nostr.ws.Connection connect
INFO: The session is now open to relay.badgr.space
Oct 17, 2023 1:17:51 AM nostr.ws.request.handler.provider.DefaultRequestHandler sendMessage
INFO: Sending Message: ["REQ","si4n6r-pool-2-thread-1",{"kinds":[24133]}]
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Processing event GenericEvent(id=c5edfb62e1f3fa34f4544a9322d1856e55f2457c709e8b3c0b16739b9b77935a, pubKey=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, createdAt=1697505483, kind=24133, tags=[PubKeyTag(publicKey=9cb64796ed2c5f18846082cae60c3a18d7a506702cdff0276f86a2ea68a94123, mainRelayUrl=null, petName=null), PubKeyTag(publicKey=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, mainRelayUrl=null, petName=null)], content=zyDi8iEGz9GTMXn5TsopnQ/vmNKrKxaJUfqpsT4WQW7RDXWlKaVCAfjmiMhDB1cXycql0Yox5njKW6xsZO/qvuIEVlbYD/0F5/q7UV3+D6HKMkcwFhDv9k+OQiwqRTFWC6Tj0fsaKp5v9da8ajpp70lv4iaSzFt1EcfCjDimRFaEyhMK5HgH+gQooEexXySopiPLMOnCfX2f+tAWAQqh/w==?iv=NPgQcKVp1gp5JDGPBGSK/w==, signature=190d8a30b1767f5bc0b830924166309048348222ffe523eb44bcc53f612ede1c772070c2b0edd7a379d93a09050b391187c7c6a36beb47343511a076983ec6f2, _serializedEvent=null, nip=null, attributes=[])
Oct 17, 2023 1:18:04 AM nostr.api.NIP04 decrypt
INFO: The message is being decrypted for e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Content: {"id":"aa35144b-8280-4e15-be16-37366bbbee65","method":"connect","params":["e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf"],"sessionId":null}
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Request: Request(id=aa35144b-8280-4e15-be16-37366bbbee65, app=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, method=Method(params=[Parameter(name=pubkey, value=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf)], result=null, getName=connect), sessionId=null)
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Method: connect
Oct 17, 2023 1:18:04 AM nostr.si4n6r.core.Session <init>
INFO: Creating new session...
Oct 17, 2023 1:18:04 AM nostr.si4n6r.core.SessionManager addRequest
INFO: Linking request Request(id=aa35144b-8280-4e15-be16-37366bbbee65, app=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, method=Method(params=[Parameter(name=pubkey, value=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf)], result=null, getName=connect), sessionId=null) to session 3ecb31cb-1517-4131-921c-192c8fc4b1b1
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.SignerService handle
INFO: Handling Request(id=aa35144b-8280-4e15-be16-37366bbbee65, app=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, method=Method(params=[Parameter(name=pubkey, value=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf)], result=null, getName=connect), sessionId=3ecb31cb-1517-4131-921c-192c8fc4b1b1)
Oct 17, 2023 1:18:04 AM nostr.si4n6r.core.ConnectionManager connect
INFO: e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf connected!
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.SignerService connect
INFO: ACK: e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf connected!
Oct 17, 2023 1:18:04 AM nostr.api.factory.impl.NIP46$NostrConnectEventFactory <init>
INFO: NostrConnectEventFactory Sender: 9cb64796ed2c5f18846082cae60c3a18d7a506702cdff0276f86a2ea68a94123 - Response: {"id":"aa35144b-8280-4e15-be16-37366bbbee65","method":"connect","result":"ACK","error":null,"sessionId":"3ecb31cb-1517-4131-921c-192c8fc4b1b1"}
Oct 17, 2023 1:18:04 AM nostr.si4n6r.signer.SignerService handle
INFO: Submitting event EphemeralEvent()
Oct 17, 2023 1:18:04 AM nostr.client.Client send
INFO: Sending message EventMessage(super=BaseMessage(command=EVENT), event=EphemeralEvent(), subscriptionId=null)
Oct 17, 2023 1:18:04 AM nostr.ws.ClientListenerEndPoint onConnect
INFO: Connected to relay relay.badgr.space/74.208.24.224:443
Oct 17, 2023 1:18:04 AM nostr.ws.Connection connect
INFO: The session is now open to relay.badgr.space
Oct 17, 2023 1:18:04 AM nostr.ws.request.handler.provider.DefaultRequestHandler sendMessage
INFO: Sending Message: ["EVENT",{"id":"37f87ece613049d4792736c0b8507fd4cc05e3c5c945685772a00711a2b17490","kind":24133,"content":"ETPTFs0xf3pGPrM9JZhetgqRdH6VQ8urZCmC82FrwOJqp627AhGgFOOdVpN88/SoaVRuLkkY8B1qPLj2oa7i5oM200rKSeqwEoF69jrpjev0TAWihN3O+kQYKpMAhZ0Bxx8OzxM4W4uMVRP2XqDHzPgPj6vRd5QrnxuLjmPLJfsaZRmLAFAX/QNvFVorrG4H?iv=uwDH+5QK0Jo2/YzGVmgAkQ==","pubkey":"9cb64796ed2c5f18846082cae60c3a18d7a506702cdff0276f86a2ea68a94123","created_at":1697505484,"tags":[["p","e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf"]],"sig":"43dc68058fbe8e6865eaea0a80d0dcb8cce0dea68dbe1056eb24be5c3949c5448e93cbb8e54816d9e669d91a9f1830cbbe029cc5dec76dc5c94c52d5d3452b4e"}]
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Processing event GenericEvent(id=521d7cdb29ee6360cba3af9ce2349066b1bfd9289a933f1be73580101aaaea21, pubKey=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, createdAt=1697505484, kind=24133, tags=[PubKeyTag(publicKey=9cb64796ed2c5f18846082cae60c3a18d7a506702cdff0276f86a2ea68a94123, mainRelayUrl=null, petName=null), PubKeyTag(publicKey=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, mainRelayUrl=null, petName=null)], content=uuEieiXnFNYdUILzVDeqqMyPPDI59KGiKB70klxiJ5RDjvUpZq/h+Eq+SnjDalnYR2TyOcbXYmjsP2IQRtmT5Dnvx3Rou0cwtt349nBMzOCmLmceh0jYYMb30arScS38?iv=qSy7USpSkWq+MBGdPQhs2w==, signature=3bd454587af65ec4233a6454b381e7fe9fb7385192ffa0319e1f1298a752895281dc435e816aaff87f2ecdfd7b79d67683a96b3ab575f7f0c57905c58e5a665c, _serializedEvent=null, nip=null, attributes=[])
Oct 17, 2023 1:18:05 AM nostr.api.NIP04 decrypt
INFO: The message is being decrypted for e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Content: {"id":"b4da63e8-c4b9-4135-af4e-a18243a191cf","method":"describe","params":[],"sessionId":null}
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Request: Request(id=b4da63e8-c4b9-4135-af4e-a18243a191cf, app=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, method=Method(params=[], result=null, getName=describe), sessionId=null)
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.provider.SignerCommandHandler onEvent
INFO: Method: describe
Oct 17, 2023 1:18:05 AM nostr.si4n6r.core.SessionManager addRequest
INFO: Linking request Request(id=b4da63e8-c4b9-4135-af4e-a18243a191cf, app=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, method=Method(params=[], result=null, getName=describe), sessionId=null) to session 3ecb31cb-1517-4131-921c-192c8fc4b1b1
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.SignerService handle
INFO: Handling Request(id=b4da63e8-c4b9-4135-af4e-a18243a191cf, app=e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf, method=Method(params=[], result=null, getName=describe), sessionId=3ecb31cb-1517-4131-921c-192c8fc4b1b1)
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.SignerService describe
INFO: describe: [describe, connect, disconnect]
Oct 17, 2023 1:18:05 AM nostr.api.factory.impl.NIP46$NostrConnectEventFactory <init>
INFO: NostrConnectEventFactory Sender: 9cb64796ed2c5f18846082cae60c3a18d7a506702cdff0276f86a2ea68a94123 - Response: {"id":"b4da63e8-c4b9-4135-af4e-a18243a191cf","method":"describe","result":"[describe, connect, disconnect]","error":null,"sessionId":"3ecb31cb-1517-4131-921c-192c8fc4b1b1"}
Oct 17, 2023 1:18:05 AM nostr.si4n6r.signer.SignerService handle
INFO: Submitting event EphemeralEvent()
Oct 17, 2023 1:18:05 AM nostr.client.Client send
INFO: Sending message EventMessage(super=BaseMessage(command=EVENT), event=EphemeralEvent(), subscriptionId=null)
Oct 17, 2023 1:18:05 AM nostr.ws.ClientListenerEndPoint onConnect
INFO: Connected to relay relay.badgr.space/74.208.24.224:443
Oct 17, 2023 1:18:05 AM nostr.ws.Connection connect
INFO: The session is now open to relay.badgr.space
Oct 17, 2023 1:18:05 AM nostr.ws.request.handler.provider.DefaultRequestHandler sendMessage
INFO: Sending Message: ["EVENT",{"id":"01d83d8f759f4722bf2ca24c6e832adf489acca934c4aa0b0b3cad2a88b57bc5","kind":24133,"content":"6iSvfylPlEn3sk3L8TU7Wma4MLsIDfFypkF7dGGjyz04Sot/+JSzZgAlP/Ht/bwmO5Q6SI+XNn0yTEeKp96zKox4il92jLptg0ib+i8i63qGP9rzv3CGffoqU6ltBNrEMVZl1xddr8DzCSyrlny99Smy40z4g6/A7baDnE4GMl1OLY0iA6wTgIm/6HJFqQ1Kff3dwD0MhZSsWb2SPPcxdHnMgoEX8TMd8jnyRIxyEk0=?iv=qdLgRmOOn6Ftd/5z6+XdAg==","pubkey":"9cb64796ed2c5f18846082cae60c3a18d7a506702cdff0276f86a2ea68a94123","created_at":1697505485,"tags":[["p","e4dda4da3b9ee71d835c0e387d5006ddf908750f1800a6c304d54e429892bdbf"]],"sig":"994f6990b107ba42ea0bd893268f2d83c503387e8c83b69df5f23dc3203c43295b9008cc8194783479239df5170f6cedc034c00449e8a36d7cb2cbc3b1617b9e"}]
^COct 17, 2023 1:32:56 AM nostr.si4n6r.server.Daemon lambda$main$0
INFO: Shutting down the si4n6r daemon service...
```
## TODO

1. Session Management (done, but untested)
2. Implement additional NIP-46 methods
3. Private key management and storage
4. Persistence
5. Improve logging
6. Testing