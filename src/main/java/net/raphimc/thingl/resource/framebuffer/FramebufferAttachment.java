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

package net.raphimc.thingl.resource.framebuffer;

import net.raphimc.thingl.resource.renderbuffer.AbstractRenderBuffer;
import net.raphimc.thingl.resource.texture.AbstractTexture;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.opengl.GL45C;

public interface FramebufferAttachment {

    static FramebufferAttachment fromGlId(final int framebufferGlId, final int attachment) {
        final int type = de.florianmichael.thingl.GlCommands.get().glGetNamedFramebufferAttachmentParameteri(framebufferGlId, attachment, GL30C.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE); // FlorianMichael - add macOS support
        if (type == GL11C.GL_NONE) {
            return null;
        }

        final int attachmentGlId = de.florianmichael.thingl.GlCommands.get().glGetNamedFramebufferAttachmentParameteri(framebufferGlId, attachment, GL30C.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME); // FlorianMichael - add macOS support
        return switch (type) {
            case GL11C.GL_TEXTURE -> AbstractTexture.fromGlId(attachmentGlId);
            case GL30C.GL_RENDERBUFFER -> AbstractRenderBuffer.fromGlId(attachmentGlId);
            default -> throw new IllegalArgumentException("Unsupported framebuffer attachment type: " + type);
        };
    }

    int getGlType();

    int getGlId();

    int getWidth();

    int getHeight();

    void free();

}
