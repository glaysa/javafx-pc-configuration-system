package org.openjfx.file_utilities.file_io;

import org.openjfx.file_utilities.FileReaders;
import org.openjfx.file_utilities.FileWriters;
import java.io.File;
import java.util.ArrayList;

public class IO_bin implements FileWriters, FileReaders {

    @Override
    public <T> ArrayList<T> read(File file) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    @Override
    public <T> void write(ArrayList<T> toSave, File filename) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }
}
