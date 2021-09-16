package cn.magicalsheep.csunoticeapi.model.packet;

public interface Packet {

    enum TYPE {
        LOGIN
    }

    TYPE getType();
}
