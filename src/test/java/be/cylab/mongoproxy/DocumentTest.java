/*
 * The MIT License
 *
 * Copyright 2018 tibo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package be.cylab.mongoproxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author tibo
 */
public class DocumentTest {

    @Test
    public void testConstructor() throws IOException {
        byte[] msg = readFile("msg_1525942174707");
        Document doc = new Document(msg, 38);
        System.out.println(doc);

        assertEquals(3, doc.size());

        Element element_0 = doc.get(0);
        assertTrue(element_0.isString());
        assertEquals("insert", element_0.getName());
        assertEquals("myCollection", element_0.value());

        Element element_1 = doc.get(1);
        assertTrue(element_1.isBoolean());
        assertEquals("ordered", element_1.getName());
        assertEquals(true, element_1.value());

        Element element_2 = doc.get(2);
        assertTrue(element_2.isDocument());
        assertEquals("documents", element_2.getName());

        Document inserted_documents = (Document) element_2.value();
        assertEquals(1, inserted_documents.size());


        Document inserted_document =
                (Document) inserted_documents.get(0).value();

        // inserted document has an id + a single key-value pair
        assertEquals(2, inserted_document.size());
        assertTrue(inserted_document.get(0).isObjectId());
        assertTrue(inserted_document.get(1).isString());

        assertEquals("key", inserted_document.get(1).getName());
        assertEquals("value", inserted_document.get(1).value());

    }

    private byte[] readFile(String string) throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(string);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nread;
        byte[] data = new byte[16384];
        while ((nread = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nread);
        }
        buffer.flush();

        return buffer.toByteArray();
    }

}
