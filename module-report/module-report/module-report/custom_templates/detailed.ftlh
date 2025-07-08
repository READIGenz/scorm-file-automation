<#escape x as x?html?replace("\n","</br><br>")>
    <!DOCTYPE html>
    <html>
    <head xmlns="http://www.w3.org/1999/xhtml">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <style>
            .collapsible {
                background-color: #c3e2fc;
                color: white;
                cursor: pointer;
                padding: 10px;
                width: 100%;
                text-align: left;
                outline: #b9daff;
                font-size: 15px;
                font-weight: bold;
                transition: max-height 10s ease-out;
                border-radius: 8px;
                box-shadow: 0 12px 16px 0 rgba(0, 0, 0, 0.24), 0 17px 50px 0 rgba(0, 0, 0, 0.19);
                -webkit-transition-duration: 0.4s; /* Safari */
                transition-duration: 0.4s;
            }

            .active, .collapsible:hover {
                background-color: #b9daff;
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

        </style>
        <style type="text/css">
            html, body, h1, h2, h3, h4, h5, h6 {
                background: #ffffff; /* Old browsers */
                font-family: Arial, sans-serif;
            }

            h1 {
                margin-top: 50px;
                color: white;
                padding-left: 10px;
                background: #74bbd9; /* Old browsers */
                background: -moz-linear-gradient(top, #74bbd9 0%, #54a3ee 50%, #3690f0 51%, #44a4cb 100%); /* FF3.6-15 */
                background: -webkit-linear-gradient(top, #74bbd9 0%, #54a3ee 50%, #3690f0 51%, #44a4cb 100%); /* Chrome10-25,Safari5.1-6 */
                background: linear-gradient(to bottom, #74bbd9 0%, #54a3ee 50%, #3690f0 51%, #44a4cb 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
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
                background: -moz-linear-gradient(top, #49c0f0 0%, #2cafe3 100%); /* FF3.6-15 */
                background: -webkit-linear-gradient(top, #49c0f0 0%, #2cafe3 100%); /* Chrome10-25,Safari5.1-6 */
                background: linear-gradient(to bottom, #49c0f0 0%, #2cafe3 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
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
                background: -moz-linear-gradient(top, #74bbd9 0%, #54a3ee 50%, #3690f0 51%, #44a4cb 100%); /* FF3.6-15 */
                background: -webkit-linear-gradient(top, #74bbd9 0%, #54a3ee 50%, #3690f0 51%, #44a4cb 100%); /* Chrome10-25,Safari5.1-6 */
                background: linear-gradient(to bottom, #74bbd9 0%, #54a3ee 50%, #3690f0 51%, #44a4cb 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
                filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#74bbd9', endColorstr='#44a4cb', GradientType=0); /* IE6-9 */
                color: black;
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

            span a {
                color: black;
            }

            span a:hover {
                font-weight: bold;
                color: #0000ff;
            }

            table, tr, td, th {
                border-collapse: collapse;
            }

            tr.mainHeader {
                cursor: pointer;
            }

            table {
                border: 1px solid #cddfe4;
                border-collapse: collapse;
                width: 100%;
            }

            .chart {
                border: 1px solid #cddfe4;
                border-collapse: collapse;
                background-color: none;
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
                background-color: transparent;
                font-weight: normal;
                color: red;
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
                border: none;
                border-collapse: collapse;
                vertical-align: top;
            }

            span.passed {
                border: 0px none;
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

            ol a::after {
                content: leader('.') target-counter(attr(href), page);
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
                color: #0000ff;
            }

            .a1 {
                color: #333333;
                text-decoration: none;
            }


            @page {
                size: A4 landscape;
                @top-center {
                    content: "Overall Results";
                    color: silver;
                    font-size: 14px;
                }
                @top-right {
                    content: date("dd MMM, yyyy hh:mm");
                    color: silver;
                    font-size: 8px;
                }
                @bottom-right {
                    content: "Page " counter(page) " of " counter(pages) ;
                    color: silver;
                    font-size: 8px;
                }
            }



        </style>
        <title>${title}</title>
        <meta charset="UTF-8"/>
    </head>
    <body>
    <div class='header'></div>
    <div class='footer'></div>
    <#include "tables">
    <!---->
    <!--<h1>Overview</h1>-->

    <!--<@drawOverviewStatsTable stats=stats />-->

    <a id="top"></a>
    <h1>Table of Contents</h1>


    <#list results as result>
        <button type="button" class="collapsible"><a class="a a1"
                    href="#feature-${result.getId()}">${result.getName()}</a></button>
        <ol>
            <#list result.getElements() as scenario>
                <div class="${scenario.getStatus()}">
                    <#if scenario.getKeyword()?starts_with("Scenario")>
                        <li><span class="${scenario.getStatus()}">
                                <a class="${scenario.getStatus()}"
                                        href="#sc-${scenario.getId()}">${scenario.getName()}</a></span></li>
                    </#if>
                </div>
            </#list>
        </ol>
    </#list>

    <h1>Detailed Results Report</h1>

    <table width="700px" border="0">

        <#list results as result>
            <tr class="${result.getStatus()}">
                <td colspan="4"><b>Feature:</b> <a id="feature-${result.getId()}">${result.getName()}</a></td>
            </tr>

            <#if result.getDescription()?has_content>
                <tr class="${result.getStatus()}_description">
                    <td colspan="4"><br>${result.getDescription()}</br></td>
                </tr>
            </#if>

            <tr class="${result.getStatus()}">
                <td><small><b>Passed:</b> ${result.getPassed()}</small></td>
                <td><small><b>Failed:</b> ${result.getFailed()}</small></td>
                <td><small><b>Undefined:</b> ${result.getUndefined() + result.getSkipped()}</small></td>
                <td><small>Duration: ${result.getDuration()?string[".##"]}s</small></td>
            </tr>
            <div>
                <tr>
                    <td colspan="4" style="padding-left:20px">
                        <table width="100%">
                            <#list result.getElements() as scenario>
                                <tr class="${scenario.getStatus()}">
                                    <td colspan="4">
                                        <b>${scenario.getKeyword()}:</b> <a
                                                id="sc-${scenario.getId()}">${scenario.getName()}</a>
                                    </td>
                                </tr>
                                <#if scenario.getDescription()?has_content>
                                    <tr class="${scenario.getStatus()}_description">
                                        <td colspan="4"><br>${scenario.getDescription()}</br></td>
                                    </tr>
                                </#if>
                               <#-- <tr class="${scenario.getStatus()}">
                                    <td><small><b>Passed:</b> ${scenario.getPassed()}</small></td>
                                    <td><small><b>Failed:</b> ${scenario.getFailed()}</small></td>
                                    <td><small><b>Undefined:</b> ${scenario.getUndefined() + scenario.getSkipped()}
                                        </small>
                                    </td>
                                    <td><small>Duration: ${scenario.getDuration()?string[".##"]}s</small></td>
                                </tr>-->
                            <#-- <#if scenario.getBefore()?has_content>
                                 <tr class="${scenario.getBefore().getResult().getStatus()}">
                                     <td>Before</td>
                                     <td colspan="2"></td>
                                     <td width="100">${scenario.getBefore().getResult().getDurationTimeString("HH:mm:ss:S")}</td>
                                 </tr>
                                 <tr class="${scenario.getBefore().getResult().getStatus()}">
                                     <td colspan="4">
                                         <#if scenario.getBefore().getResult().getErrorMessage()?has_content>
                                             <br>${scenario.getBefore().getResult().getErrorMessage()}</br>
                                         </#if>
                                     </td>
                                 </tr>
                             </#if>-->
                                <tr class="${scenario.getStatus()}">
                                    <td colspan="4" style="padding-left:20px">
                                        <table width="100%">
                                            <#list scenario.getSteps() as step>
                                                <tr class="${step.getResult().getStatus()}">
                                                    <td><b>${step.getKeyword()}</b> ${step.getName()}</td>
                                                    <td width="100">${step.getResult().getDurationTimeString("HH:mm:ss:S")}</td>
                                                </tr>
                                                <#if step.getRows()?has_content >
                                                    <tr class="${step.getResult().getStatus()}">
                                                        <td style="padding-left:20px" colspan="2">
                                                            <table>
                                                                <#list step.getRows() as row>
                                                                    <tr>
                                                                        <#list row as col>
                                                                            <td>${col}</td>
                                                                        </#list>
                                                                    </tr>
                                                                </#list>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </#if>
                                                <#if step.getDocString()?has_content>
                                                    <tr class="${step.getResult().getStatus()}_description">
                                                        <td style="padding-left:20px" colspan="2">
                                                            <br>${step.getDocString()}</br>
                                                        </td>
                                                    </tr>
                                                </#if>
                                                <#if step.result.errorMessage?has_content>
                                                    <tr class="${step.getResult().getStatus()}">
                                                        <td colspan="2">
                                                            <br>${step.result.errorMessage}</br>
                                                        </td>
                                                    </tr>
                                                </#if>
                                                <#list step.getScreenShotLocations() as screenshot>
                                                    <tr class="${step.getResult().getStatus()}">
                                                        <td colspan="2">
																	<a "${screenShotWidth}" href="${screenshot}" target="_blank" rel="noopener noreferrer">
                                                						<img src="${screenshot}" width="${screenShotWidth}">
                                            						</a>
														</td>
                                                    </tr>
                                                </#list>
                                                <#if step.getOutput()?has_content>
                                                    <tr class="${step.getResult().getStatus()}">
                                                        <td colspan="2" class="comment">
                                                            <span class="tip">Output</span>
                                                            <pre class="comment"><br>
											<#list step.getOutput() as line>
                                                ${line}
                                            </#list>
											</br>
											</pre>
                                                        </td>
                                                    </tr>
                                                </#if>
                                            </#list>
                                        </table>
                                    </td>
                                </tr>

                            <#--<#if scenario.getAfter()?has_content>
                                <tr class="${scenario.getAfter().getResult().getStatus()}">
                                    <td>After</td>
                                    <td colspan="2"></td>
                                    <td width="100">${scenario.getAfter().getResult().getDurationTimeString("HH:mm:ss:S")}</td>
                                </tr>
                                <tr class="${scenario.getAfter().getResult().getStatus()}">
                                    <td colspan="4">
                                        <#if scenario.getAfter().getResult().getErrorMessage()?has_content>
                                            <br>${scenario.getAfter().getResult().getErrorMessage()}</br>
                                        </#if>
                                    </td>
                                </tr>
                            </#if>-->
                                <tr>
                                    <td colspan="5"><sup><a href="#top">Back to Table of Contents</a></sup></td>
                                </tr>
                            </#list>
                        </table>
                    </td>
                </tr>
            </div>
            <tr>
                <td colspan="5"></td>
            </tr>
        </#list>
    </table>


    <script>
        var coll = document.getElementsByClassName('collapsible');
        var i;

        for (i = 0; i < coll.length; i++) {
            coll[i].addEventListener('click', function () {
                this.classList.toggle('active');
                var content = this.nextElementSibling;
                if (content.style.display === 'block') {
                    content.style.display = 'none';
                } else {
                    content.style.display = 'block';
                }
            });
        }
    </script>
    <script>
        $(document).ready(function () {
            $('tr:not(.mainHeader)').hide();

            $('tr.mainHeader').click(function () {
                $(this).find('span').text(function (_, value) {
                    return value == '-' ? '+' : '-';
                });

                $(this).nextUntil('tr.mainHeader').slideToggle(100, function () {
                });
            });
        });
    </script>

    </body>
    </html>
</#escape>