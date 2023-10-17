# si4n6r
A NIP-46 compliant signer

This is a signer that implements the NIP-46 standard.
It is meant to be used as a remote signer for the [demo-nip46-app] app, or any other NIP-46 compliant app of your
choice.
Right now it is a very simple signer, a proof of concept, that only supports the `connect` (signer and app
initiated), `disconnect`, and `describe` methods.

The software is composed of four parts:

- The [si4n6r-lib] library that implements the NIP-46 standard.
- The [si4n6r] binary that implements the NIP-46 standard and exposes the signer as a websocket API.
- The [shibboleth] library that implements the NIP-46 app standard to connect to a signer through a websocket API.
- The [demo-nip46-app] binary, or any other NIP-46 compliant app of your choice, that uses the [shibboleth] library to
  connect to a signer through a websocket API.

*The app and the signer are expected to run on different Virtual Machines.*

The software is written in Java and relies on the [nostr-java] library of the nostr protocol. It is currently in
pre-alpha stage, _untested_, and expected to change in the near future.
It implements

- A [Signer] and an [Application] classes, with their own identities (public and private key pairs) for communication.
- A [SignerService] to submit the connect request to the signer, handle the incoming requests from the app, and send the
  responses back to the app.
- An [ApplicationService] to submit requests to the signer and handle the incoming responses from the signer.
- A [SignerCommandHandler] and an [ApplicationCommandHandler] to handle the incoming events from the app and the signer
  respectively.

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

Here, it is assumed that you are running the [nostr-rs] relay on your localhost, on port 8080.

### Signer identity

You configure the signer identity in the `signer.properties` file in your `$HOME/.nostr-java/identities` folder, on the
server. Here, it is assumed that your signer holds the identity with name `signer`

```
privateKey=519672a62831011....5b3998a1d65366aff97988
```

(The keys are in hex format)

## Installation and Usage

1. Build the [nostr-java] project. I am currently using the 0.3-SNAPSHOT branch of [nostr-java]
2. Build the [si4n6r-lib] project
3. Build the [si4n6r] project
4. Build the [shibboleth] project
5. Build the [demo-nip46-app] project
6. Run the [si4n6r] binary on your server
7. Run the [demo-nip46-app] binary on your client

## TODO

1. Session Management (done, but untested)
2. Implement additional NIP-46 methods
3. Private key management and storage
4. Persistence
5. Improve logging
6. Testing