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
package net.raphimc.thingl.wrapper;

import net.raphimc.thingl.ThinGL;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL14C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;

public class GLStateManager {

    @ApiStatus.Internal
    public GLStateManager(final ThinGL thinGL) {
    }

    public boolean getCapability(final int capability) {
        return GL11C.glIsEnabled(capability);
    }

    public void enable(final int capability) {
        this.setCapability(capability, true);
    }

    public void disable(final int capability) {
        this.setCapability(capability, false);
    }

    public void setCapability(final int capability, final boolean state) {
        if (state) {
            GL11C.glEnable(capability);
        } else {
            GL11C.glDisable(capability);
        }
    }

    public BlendFunc getBlendFunc() {
        return new BlendFunc(
                GL11C.glGetInteger(GL14C.GL_BLEND_SRC_RGB),
                GL11C.glGetInteger(GL14C.GL_BLEND_DST_RGB),
                GL11C.glGetInteger(GL14C.GL_BLEND_SRC_ALPHA),
                GL11C.glGetInteger(GL14C.GL_BLEND_DST_ALPHA)
        );
    }

    public void setBlendFunc(final int src, final int dst) {
        this.setBlendFunc(src, dst, src, dst);
    }

    public void setBlendFunc(final int srcRGB, final int dstRGB, final int srcAlpha, final int dstAlpha) {
        GL14C.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
    }

    public int getDepthFunc() {
        return GL11C.glGetInteger(GL11C.GL_DEPTH_FUNC);
    }

    public void setDepthFunc(final int func) {
        GL11C.glDepthFunc(func);
    }

    public ColorMask getColorMask() {
        final int[] colorMask = new int[4];
        GL11C.glGetIntegerv(GL11C.GL_COLOR_WRITEMASK, colorMask);
        return new ColorMask(colorMask[0] != GL11C.GL_FALSE,
                colorMask[1] != GL11C.GL_FALSE,
                colorMask[2] != GL11C.GL_FALSE,
                colorMask[3] != GL11C.GL_FALSE);
    }

    public void setColorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        GL11C.glColorMask(red, green, blue, alpha);
    }

    public boolean getDepthMask() {
        return GL11C.glGetBoolean(GL11C.GL_DEPTH_WRITEMASK);
    }

    public void setDepthMask(final boolean state) {
        GL11C.glDepthMask(state);
    }

    public Scissor getScissor() {
        final int[] scissor = new int[4];
        GL11C.glGetIntegerv(GL11C.GL_SCISSOR_BOX, scissor);
        return new Scissor(scissor[0], scissor[1], scissor[2], scissor[3]);
    }

    public void setScissor(final int x, final int y, final int width, final int height) {
        GL11C.glScissor(x, y, width, height);
    }

    public Viewport getViewport() {
        final int[] viewport = new int[4];
        GL11C.glGetIntegerv(GL11C.GL_VIEWPORT, viewport);
        return new Viewport(viewport[0], viewport[1], viewport[2], viewport[3]);
    }

    public void setViewport(final int x, final int y, final int width, final int height) {
        GL11C.glViewport(x, y, width, height);
    }

    public int getLogicOp() {
        return GL11C.glGetInteger(GL11C.GL_LOGIC_OP_MODE);
    }

    public void setLogicOp(final int op) {
        GL11C.glLogicOp(op);
    }

    public PolygonOffset getPolygonOffset() {
        return new PolygonOffset(
                GL11C.glGetFloat(GL11C.GL_POLYGON_OFFSET_FACTOR),
                GL11C.glGetFloat(GL11C.GL_POLYGON_OFFSET_UNITS)
        );
    }

    public void setPolygonOffset(final float factor, final float units) {
        GL11C.glPolygonOffset(factor, units);
    }

    public int getPixelStore(final int parameter) {
        return GL11C.glGetInteger(parameter);
    }

    public void setPixelStore(final int parameter, final int value) {
        GL11C.glPixelStorei(parameter, value);
    }

    public int getProgram() {
        return GL11C.glGetInteger(GL20C.GL_CURRENT_PROGRAM);
    }

    public void setProgram(final int program) {
        GL20C.glUseProgram(program);
    }

    public int getVertexArray() {
        return GL11C.glGetInteger(GL30C.GL_VERTEX_ARRAY_BINDING);
    }

    public void setVertexArray(final int vertexArray) {
        GL30C.glBindVertexArray(vertexArray);
    }

    public record BlendFunc(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
    }

    public record ColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    }

    public record Scissor(int x, int y, int width, int height) {
    }

    public record Viewport(int x, int y, int width, int height) {
    }

    public record PolygonOffset(float factor, float units) {
    }

}
