package dev.phonis.cannonliner.networking;

import java.io.DataOutputStream;
import java.io.IOException;

public interface CTSerializable {

    void toBytes(DataOutputStream dos) throws IOException;

}
