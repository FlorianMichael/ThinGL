/*
 * This file is part of ThinGL - https://github.com/RaphiMC/ThinGL
 * Copyright (C) 2024-2025 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.raphimc.thingl.resource.buffer;

import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL45C;

import java.nio.ByteBuffer;

public class Buffer extends AbstractBuffer {

    private final int usage;

    public Buffer(final long size, final int usage) {
        super(size);
        this.usage = usage;
        de.florianmichael.thingl.GlCommands.get().glNamedBufferData(this.getGlId(), size, usage); // FlorianMichael - add macOS support
    }

    public Buffer(final ByteBuffer data, final int usage) {
        super((long) data.remaining());
        this.usage = usage;
        de.florianmichael.thingl.GlCommands.get().glNamedBufferData(this.getGlId(), data, usage); // FlorianMichael - add macOS support
    }

    protected Buffer(final int glId) {
        super(glId);
        this.usage = GL45C.glGetNamedBufferParameteri(glId, GL15C.GL_BUFFER_USAGE);
    }

    @Override
    public void refreshCachedData() {
        this.size = GL45C.glGetNamedBufferParameteri64(this.getGlId(), GL15C.GL_BUFFER_SIZE);
    }

    public void setSize(final long size) {
        this.size = size;
        de.florianmichael.thingl.GlCommands.get().glNamedBufferData(this.getGlId(), size, this.usage); // FlorianMichael - add macOS support
    }

    public int getUsage() {
        return this.usage;
    }

}
