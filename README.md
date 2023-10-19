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
- The [si4n6r](https://github.com/tcheeric/si4n6r/tree/1.0-SNAPSHOT), the signer binary.
- The [shibboleth](https://github.com/tcheeric/shibboleth/tree/1.0-SNAPSHOT) app library that implements the NIP-46 app
  standard to connect to a signer through a websocket API.
- The [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) binary, a sample NIP-46
  compliant app, that uses the [Shibboleth](https://github.com/tcheeric/shibboleth/tree/1.0-SNAPSHOT)
  app library to connect to a signer through a websocket API.

NOTE: The app and the signer are expected to run on different Virtual Machines.

The software is written in Java and relies on the [nostr-java](https://github.com/tcheeric/nostr-java/tree/0.3-SNAPSHOT)
library of the nostr protocol. It is currently in pre-alpha stage, it's still _untested_, and is expected to change
significantly in the near future.

The most important classes are:

- The [Signer](https://github.com/tcheeric/si4n6r-lib/blob/1.0-SNAPSHOT/si4n6r-signer/src/main/java/nostr/si4n6r/signer/Signer.java)
and the [Application](https://github.com/tcheeric/shibboleth/blob/1.0-SNAPSHOT/src/main/java/nostr/si4n6r/shibboleth/Application.java) classes, with their own identities (public and private key pairs) for communication.
- The [SignerService](https://github.com/tcheeric/si4n6r-lib/blob/1.0-SNAPSHOT/si4n6r-signer/src/main/java/nostr/si4n6r/signer/SignerService.java)
to submit the signer-initiated connect request to the app, handle the incoming requests from the app, and send the responses back to the app.
- The [ApplicationService](https://github.com/tcheeric/shibboleth/blob/1.0-SNAPSHOT/src/main/java/nostr/si4n6r/shibboleth/AppService.java) to submit requests to the signer and handle the incoming responses from the signer.
- The [SignerCommandHandler](https://github.com/tcheeric/si4n6r-lib/blob/1.0-SNAPSHOT/si4n6r-signer/src/main/java/nostr/si4n6r/signer/provider/SignerCommandHandler.java) and an [ApplicationCommandHandler](https://github.com/tcheeric/shibboleth/blob/1.0-SNAPSHOT/src/main/java/nostr/si4n6r/shibboleth/provider/AppCommandHandler.java) to handle the incoming events from the app and the signer respectively.

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

1. Build the [nostr-java](https://github.com/tcheeric/nostr-java/tree/0.3-SNAPSHOT) project. (I am currently using the
   0.3-SNAPSHOT branch)
2. Build the [si4n6r-lib](https://github.com/tcheeric/si4n6r-lib/tree/1.0-SNAPSHOT) project
3. Build the [si4n6r](https://github.com/tcheeric/si4n6r/tree/1.0-SNAPSHOT) project
4. Build the [shibboleth](https://github.com/tcheeric/shibboleth/tree/1.0-SNAPSHOT) project
5. Build the [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) project
6. Run the [si4n6r](https://github.com/tcheeric/si4n6r/tree/1.0-SNAPSHOT) binary on your server
7. Run the [demo-nip46-app](https://github.com/tcheeric/demo-nip46-app/tree/1.0-SNAPSHOT) binary on your client

## TODO

1. Session Management (done, but untested)
2. Persistence (??)
3. Interactivity
4. Testing
5. Improve logging
6. Implement additional NIP-46 methods
7. Private key management and storage

