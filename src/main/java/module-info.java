module si4n6r.server {

    requires si4n6r.signer;
    requires si4n6r.core;

    requires lombok;
    requires java.logging;

    requires nostr.api;
    requires nostr.base;
    requires nostr.event;

    exports nostr.si4n6r.server;
}