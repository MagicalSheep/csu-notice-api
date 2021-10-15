package cn.magicalsheep.csunoticeapi.common.model.packet;

public interface Packet {

    enum TYPE {
        LOGIN
    }

    TYPE getType();
}
