<#function to_id text>
    <#return text?lower_case?replace("[^a-z0-9]", "-", "r")>
</#function>
<!DOCTYPE html>
<html>
<head xmlns="http://www.w3.org/1999/xhtml">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <style type="text/css">
        .collapsible {
            background-color: #49c0f0;
            color: white;
            cursor: pointer;
            padding: 10px;
            width: 100%;
            text-align: left;
            outline: #e2feba;
            font-size: 15px;
            font-weight: bold;
            transition: max-height 10s ease-out;
            border-radius: 8px;
            box-shadow: 0 12px 16px 0 rgba(0, 0, 0, 0.24), 0 17px 50px 0 rgba(0, 0, 0, 0.19);
            -webkit-transition-duration: 0.4s; /* Safari */
            transition-duration: 0.4s;
        }

        .active, .collapsible:hover {
            background-color: #e2feba;
            font-size: 15.5px;
        }

        .collapsible:after {
            content: '\002B';
            color: black;
            font-weight: bold;
            float: right;
            margin-left: 5px;
        }

        .active:after {
            content: "\2212";
        }

        .contents {
            padding: 0 18px;
            max-height: 0;
            overflow: hidden;
            transition: max-height 0.2s ease-out;
            width: 100%;
        }

        html, body, h1, h2, h3, h4, h5, h6 {
            background: #ffffff; /* Old browsers */
            font-family: "Arial", sans-serif;
        }

        h1 {
            margin-top: 50px;
            color: white;
            padding-left: 10px;
            background: #74bbd9; /* Old browsers */
             background: -moz-linear-gradient(top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* FF3.6-15 */
            background: -webkit-linear-gradient(top,  top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to bottom,  top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#74bbd9', endColorstr='#44a4cb', GradientType=0); /* IE6-9 */
            font-weight: normal;
            font-size: larger;
            height: 50px;
            line-height: 50px;

        }

        h2 {
            color: white;
            padding-left: 10px;
            background: #49c0f0; /* Old browsers */
            background: -moz-linear-gradient(top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* FF3.6-15 */
            background: -webkit-linear-gradient(top,  top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to bottom,  top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
           filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#49c0f0', endColorstr='#2cafe3', GradientType=0); /* IE6-9 */
            font-size: large;
            height: 30px;
            line-height: 30px;
            font-weight: normal;
            margin-top: 20px;
            margin-bottom: 2px;

        }

        h3 {
            background-color: #f1f4e7
        }

        th {
            border: 2px solid #cddfe4;
            background: #74bbd9; /* Old browsers */
            background: -moz-linear-gradient(top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* FF3.6-15 */
            background: -webkit-linear-gradient(top,  top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to bottom,  top,  #004767 0%, #004740 50%, #004730 35%, #004767 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
           filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#74bbd9', endColorstr='#44a4cb', GradientType=0); /* IE6-9 */
            color: white;
            height: 30px;
            line-height: 30px;
            font-weight: normal;
        }

        td {
            border: 1px solid #cddfe4;
            padding: 5px;

        }

        tr {
            color: #090080d6;
            font-weight: bold;
        }

        table, tr, td, th {
            border-collapse: collapse;
        }

        table {
            border: 1px solid #004767;
            border-collapse: collapse;
            width: 100%;
        }

        span a {
            color: black;
        }

        span a:hover {
            font-weight: bold;
            color: #0000ff;
        }


        a {
            color: #D2691E
        }

        b {
            color: #0000ff;
        }

        .chart {
            border: 1px solid #cddfe4;
            border-collapse: collapse;
           
        }

        .passed {
            background-color: transparent;
            font-weight: normal;
            color: forestgreen;
            /*border: 1px solid green;*/
        }

        .skipped {
            background-color: silver;
            font-weight: bold;
            color: darkgray
        }

        .failed {
            background-color: white;
            font-weight: normal;
            color: red;
            border: 1px solid red;

        }

        .undefined {
            background-color: white;
            font-weight: bold;
            color: goldenrod
        }

        .lowest {
            background-color: lightyellow;
            color: black
        }

        .low {
            background-color: gold;
            color: black
        }

        .medium {
            background-color: orange;
            color: black
        }

        .high {
            background-color: orangered;
            color: black
        }

        .highest {
            background-color: darkred;
            color: black
        }

        .passed_description {
            background-color: #D0FFD0;
            font-weight: bold;
            font-style: italic;
            color: black
        }

        .failed_description {
            background-color: #FFA898;
            font-weight: bold;
            font-style: italic;
            color: black
        }

        .noborder {
            border: transparent;
            border-collapse: collapse;
            vertical-align: top;
        }

        span.passed {
            border: 0px transparent;
        }

        a.failed {
            color: red;
        }

        span.failed {
            animation: blinker 2s linear infinite;
        }

        @keyframes blinker {
            50% {
                opacity: 0;
            }
        }

        OL {
            counter-reset: item
        }

        OL > LI {
            display: block
        }

        OL > LI:before {
            content: counters(item, ".") " ";
            counter-increment: item
        }

        div.header {
            display: block;
            text-align: center;
            position: running(header);
        }

        div.footer {
            display: block;
            text-align: center;
            position: running(footer);
        }

        div.content {
            page-break-after: always;
        }

        div.content:last-child {
            page-break-after: avoid;
        }

        a {
            color: #D2691E
        }

        b {
            color: #0000FF;
        }

        .a1 {
            color: #333333;
            text-decoration: transparent;
        }

        @page {
            size: A4 landscape;
            @top-center {
                content: "Overall Results";
                color: silver;
                font-size: 14px;
            }
            @top-right {
                /*content: date("dd MMM, yyyy hh:mm");*/
                color: silver;
                font-size: 8px;
            }
            @bottom-right {
                content: "Page " counter(page) " of " counter(pages) ;
                color: silver;
                font-size: 8px;
            }
        }

        ol a::after {
            content: leader('.') target-counter(attr(href url), page);
        }
        		.projectname {
            color: black;
            padding-left: 10px;
            
            
            font-size: 25px;
            height: 40px;
            line-height: 30px;
            font-weight: bold;
            margin-top: 0px;
            margin-bottom: 2px;

        }
		.regression {
            color: black;
            padding-left: 10px;
            
            
            font-size: 20px;
            height: 40px;
            line-height: 30px;
            font-weight: bold;
            margin-top: 0px;
            margin-bottom: 2px;

        }
		.headerTable {border: none;}
    </style>
    ${refreshData}
    <title>${title}</title>
    <meta charset="UTF-8"/>
    
</head>
<body>
<div class='header'>
<table>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
<tr><p style="color:white; font-size: 1px;">.</p></tr>
</table>
<table class='headerTable'>
<tr class='headerTable'>
	<td align="left" class='headerTable'>
	  <label align="left" width="50%" class='projectname' >Test Execution Summary</label> 
	  <br>
	  <label align="left" width="50%" class='regression' >_place_holder_project_name_</label> 
	  <br>

	</td>
	<td width="50%" class='headerTable'>
		<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAN4AAAAmCAMAAACceNXxAAACzVBMVEUAAAD////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////shMKIAAAA7nRSTlMAAQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5Ozw9Pj9AQUJDREVGSElKTE1OT1BRUlNUVVZXWFlaW1xdXl9gYWJkZWZnaGlqbG1ub3BxcnN0dXZ3eHl6fH1+f4CBgoSFh4iJ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"  alt="Lufthansa Logo" align="right"/>
	</td>
</tr>
</table>
</div>
<div class='footer'></div>
<#if useTableOfContents>
    <h1>Table of Contents</h1>
    <ol>
        <#list contents?keys as section_name>
            <li><a href="#${to_id(section_name)}">${section_name}</a></li>
        </#list>
    </ol>
</#if>
<table class="noborder">
    <#assign index=0>
    <#list contents?keys as section_name>
        <#if index % columns == 0>
            <tr class="noborder" valign="top">
        </#if>
        <td class="noborder">
            <div class="content"><a id="${to_id(section_name)}"><h1>${section_name}</h1></a>
                ${contents[section_name]}
            </div>
        </td>
        <#if index % columns == (columns - 1)>
            </tr>
        </#if>
        <#assign index++>
    </#list>
    <#if index % columns != 0>
        </tr>
    </#if>
</table>
</body>
</html>