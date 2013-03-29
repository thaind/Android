/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class PXLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PXLoader() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public PXSystem load(final InputStream pInputStream) throws PXLoadException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final PXParser pxParser = new PXParser();
			xr.setContentHandler(pxParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return pxParser.getPXSystem();
		} catch (final SAXException e) {
			throw new PXLoadException(e);
		} catch (final ParserConfigurationException pe) {
			/* Doesn't happen. */
			return null;
		} catch (final IOException e) {
			throw new PXLoadException(e);
		}
	}

	public PXSystem load(final String inFilePath) throws PXLoadException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final PXParser mPXParser = new PXParser();
			sp.parse(inFilePath, mPXParser);

			return mPXParser.getPXSystem();
		} catch (final SAXException e) {
			throw new PXLoadException(e);
		} catch (final ParserConfigurationException pe) {
			throw new PXLoadException(pe);
		} catch (final IOException e) {
			throw new PXLoadException(e);
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

}
