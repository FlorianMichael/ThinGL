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

import net.raphimc.thingl.ThinGL;
import net.raphimc.thingl.resource.GLObject;
import net.raphimc.thingl.resource.framebuffer.FramebufferAttachment;
import org.lwjgl.opengl.*;

public abstract class AbstractTexture extends GLObject implements FramebufferAttachment {

    private final int type;
    private final int internalFormat;

    public AbstractTexture(final Type type, final InternalFormat internalFormat) {
        super(de.florianmichael.thingl.GlCommands.get().glCreateTextures(type.getGlType())); // FlorianMichael - add macOS support
        this.type = type.getGlType();
        this.internalFormat = internalFormat.getGlFormat();
    }

    protected AbstractTexture(final int glId, final Type type) {
        super(glId);
        this.type = type.getGlType();
        this.internalFormat = de.florianmichael.thingl.GlCommands.get().glGetTextureLevelParameteri(glId, 0, GL30C.GL_TEXTURE_INTERNAL_FORMAT); // FlorianMichael - add macOS support
    }

    public static AbstractTexture fromGlId(final int glId) {
        if (!GL11C.glIsTexture(glId)) {
            throw new IllegalArgumentException("Not a texture object");
        }
        if (!ThinGL.workarounds().isGetTextureParameterTextureTargetBroken()) {
            final Type type = Type.fromGlType(GL45C.glGetTextureParameteri(glId, GL45C.GL_TEXTURE_TARGET));
            return switch (type) {
                case TEX_2D -> new Texture2D(glId);
                case TEX_2D_MULTISAMPLE -> new MultisampleTexture2D(glId);
                default -> throw new IllegalArgumentException("Unsupported texture type: " + type.getDisplayName());
            };
        } else {
            final int samples = de.florianmichael.thingl.GlCommands.get().glGetTextureLevelParameteri(glId, 0, GL32C.GL_TEXTURE_SAMPLES); // FlorianMichael - add macOS support
            if (samples <= 0) {
                return new Texture2D(glId);
            } else {
                return new MultisampleTexture2D(glId);
            }
        }
    }

    @Override
    protected void free0() {
        de.florianmichael.thingl.GlCommands.get().glDeleteTextures(this.getGlId()); // FlorianMichael - add macOS support
    }

    @Override
    public final int getGlType() {
        return GL11C.GL_TEXTURE;
    }

    public int getType() {
        return this.type;
    }

    public Type getTypeEnum() {
        return Type.fromGlType(this.type);
    }

    public int getInternalFormat() {
        return this.internalFormat;
    }

    public InternalFormat getInternalFormatEnum() {
        return InternalFormat.fromGlFormat(this.internalFormat);
    }

    public enum Type {

        TEX_1D(GL11C.GL_TEXTURE_1D, "1D Texture"),
        TEX_2D(GL11C.GL_TEXTURE_2D, "2D Texture"),
        TEX_2D_MULTISAMPLE(GL32C.GL_TEXTURE_2D_MULTISAMPLE, "Multisample 2D Texture"),
        TEX_3D(GL12C.GL_TEXTURE_3D, "3D Texture"),
        ;

        public static Type fromGlType(final int glType) {
            for (Type type : values()) {
                if (type.glType == glType) {
                    return type;
                }
            }

            throw new IllegalArgumentException("Unknown texture type: " + glType);
        }

        private final int glType;
        private final String displayName;

        Type(final int glType, final String displayName) {
            this.glType = glType;
            this.displayName = displayName;
        }

        public int getGlType() {
            return this.glType;
        }

        public String getDisplayName() {
            return this.displayName;
        }

    }

    public enum InternalFormat {

        RGBA8(GL11C.GL_RGBA8, "RGBA8", 4),
        RGB8(GL11C.GL_RGB8, "RGB8", 3),
        RG8(GL30C.GL_RG8, "RG8", 2),
        R8(GL30C.GL_R8, "R8", 1),
        DEPTH32F(GL30C.GL_DEPTH_COMPONENT32F, "32-Bit Depth (Float)", 4),
        DEPTH32(GL14C.GL_DEPTH_COMPONENT32, "32-Bit Depth", 4),
        DEPTH24(GL14C.GL_DEPTH_COMPONENT24, "24-Bit Depth", 3),
        DEPTH16(GL14C.GL_DEPTH_COMPONENT16, "16-Bit Depth", 2),
        DEPTH32F_STENCIL8(GL30C.GL_DEPTH32F_STENCIL8, "32-Bit Depth (Float), 8-Bit Stencil", 5),
        DEPTH24_STENCIL8(GL30C.GL_DEPTH24_STENCIL8, "24-Bit Depth, 8-Bit Stencil", 4),
        RGBA_UNSIZED(GL11C.GL_RGBA, "RGBA", 4),
        ;

        public static InternalFormat fromGlFormat(final int glFormat) {
            for (InternalFormat format : values()) {
                if (format.glFormat == glFormat) {
                    return format;
                }
            }

            throw new IllegalArgumentException("Unknown texture format: " + glFormat);
        }

        private final int glFormat;
        private final String displayName;
        private final int channelCount;

        InternalFormat(final int glFormat, final String displayName, final int channelCount) {
            this.glFormat = glFormat;
            this.displayName = displayName;
            this.channelCount = channelCount;
        }

        public int getGlFormat() {
            return this.glFormat;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public int getChannelCount() {
            return this.channelCount;
        }

    }

    public enum PixelFormat {

        RGBA(GL11C.GL_RGBA, "RGBA", 4),
        RGB(GL11C.GL_RGB, "RGB", 3),
        RG(GL30C.GL_RG, "RG", 2),
        R(GL11C.GL_RED, "R", 1),
        G(GL11C.GL_GREEN, "G", 1),
        B(GL11C.GL_BLUE, "B", 1),
        A(GL11C.GL_ALPHA, "A", 1),
        BGRA(GL12C.GL_BGRA, "BGRA", 4),
        BGR(GL12C.GL_BGR, "BGR", 3),
        ;

        public static PixelFormat fromGlFormat(final int glFormat) {
            for (PixelFormat format : values()) {
                if (format.glFormat == glFormat) {
                    return format;
                }
            }

            throw new IllegalArgumentException("Unknown pixel format: " + glFormat);
        }

        private final int glFormat;
        private final String displayName;
        private final int channelCount;

        PixelFormat(final int glFormat, final String displayName, final int channelCount) {
            this.glFormat = glFormat;
            this.displayName = displayName;
            this.channelCount = channelCount;
        }

        public int getGlFormat() {
            return this.glFormat;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public int getChannelCount() {
            return this.channelCount;
        }

        public int getAlignment() {
            if (this.channelCount % Long.BYTES == 0) {
                return Long.BYTES;
            } else if (this.channelCount % Integer.BYTES == 0) {
                return Integer.BYTES;
            } else if (this.channelCount % Short.BYTES == 0) {
                return Short.BYTES;
            } else {
                return Byte.BYTES;
            }
        }

    }

}
