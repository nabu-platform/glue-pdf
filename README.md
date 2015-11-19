# Pdf

This plugin provides the ability to diff pdfs. It will convert each page into an image and then reuse the [images](https://github.com/nablex/glue-images) plugin to compare them.

```python
validatePdf("This is a test!", "original.pdf", "other.pdf")
```

The plugin will inject the visual diffs for all pages in an array variable called ``$diffImages`` so you could write out the visual difference using:

```python
for ($diffImages)
	  write("page" + $index + ".png", $value)
```

In the image plugin you can set a configuration containing the inclusion and exclusion zones for an image. In this plugin you can do the same but per page. An example:

```xml
<pdf>
	  <pages page="0">
		     <exclusionZones fromY = "300" />
	  </pages>
</pdf>
```

You can add it using:

```python
validatePdf("This is a test!", "original.pdf", "other.pdf", "config.xml")
```

Like in the image plugin you can also set the threshold:

```python
validatePdf("This is a test!", "original.pdf", "other.pdf", "config.xml", 0.03)
```

The tricky thing with PDFs is of course knowing what the pixel offset is of a zone as this information can only be deduced from the actual images, not the PDF itself. The editor can help with this.

To ignore a page, add the attribute ``ignore``:

```xml
<pdf>
	  <pages page="0" ignore="true"/>
</pdf>
```

## Editor Support

If you open a PDF resource in the editor, you get the image view as it is generated at runtime for comparisons:

![Pug 1](https://github.com/nablex/glue-pdf/blob/master/pdf-editor.png)

You can **zoom** in and out by scrolling your mouse while pressing CTRL.

If you hover over a location, you can see the **x, y** offset at the bottom.
