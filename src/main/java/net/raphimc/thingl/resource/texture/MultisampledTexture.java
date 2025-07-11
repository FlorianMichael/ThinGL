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

package net.raphimc.thingl.resource.texture;

import org.lwjgl.opengl.GL32C;
import org.lwjgl.opengl.GL45C;

public abstract class MultisampledTexture extends AbstractTexture {

    private final int samples;

    public MultisampledTexture(final Type type, final InternalFormat internalFormat, final int samples) {
        super(type, internalFormat);
        this.samples = samples;
    }

    protected MultisampledTexture(final int glId, final Type type) {
        super(glId, type);
        this.samples = de.florianmichael.thingl.GlCommands.get().glGetTextureLevelParameteri(glId, 0, GL32C.GL_TEXTURE_SAMPLES); // FlorianMichael - add macOS support
    }

    public int getSamples() {
        return this.samples;
    }

}
