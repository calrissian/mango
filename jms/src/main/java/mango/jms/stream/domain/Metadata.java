package mango.jms.stream.domain;

import java.io.Serializable;import java.lang.String;

public class Metadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2106165237211114549L;

	private int numberOfPieces;

	private long pieceSize;

	private String fileName;

	private long fileSize;

	public Metadata(int numberOfPieces, long pieceSize, String fileName,
			long fileSize) {
		super();
		this.numberOfPieces = numberOfPieces;
		this.pieceSize = pieceSize;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}

	public long getFileSize() {
		return fileSize;
	}

	public int getNumberOfPieces() {
		return numberOfPieces;
	}

	public long getPieceSize() {
		return pieceSize;
	}

	public String getFileName() {
		return fileName;
	}
}