package org.openjfx.fileUtilities;

import java.io.File;
import java.util.ArrayList;

public interface FileWriters {
    <T> void write(ArrayList<T> toSave, File file);
}
