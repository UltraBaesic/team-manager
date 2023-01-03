/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var bookid = 0, loaded_section_id = 0, selectedId = 0, loadedObjectId = 0;
var sectionText = "", selectedType = "", loadedObjectType = "", loadbookmark = false;
var current, sectionIds;
extension = GetExtension();
function GetDialogIndexList(searchtext, counted, objectid, object, section) {
    showDialogLoader("Loading Index List.....");
    $.ajax({
        url: extension + 'BookServlet', type: 'GET',
        data: {
            type: "GetAllIndexes",
            search: searchtext,
            count: counted,
            linkedObjectId: objectid,
            linkedObject: object
        },
        success: function (data) {
            if (current === "indexes") {
                var parent = $("#dialog_content");
                if (counted === 0)
                    parent.empty();
                if (data !== "none") {
                    var Ids = data[0];
                    var Header = data[1];
                    var indexSubHeaders = data[2];
                    var Bodytext = data[3];
                    $.each(Ids, function (index, id) {
                        var item = $("<div />", {class: "dialog_list_item dialog_index_item dialog_search_item marginbottom"}).appendTo(parent);
                        $("<div />", {class: "dialog_index_id", text: id}).appendTo(item).hide();
                        var left = $("<div />", {class: "parts twothirdswide left"}).appendTo(item);
                        $("<i />", {class: "fa fa-caret-square-o-down fa-fw half-marginright", click: function () {
                                $(this).closest(".dialog_index_item").find(".dialog-index-body").toggle();
                            }}).appendTo(left);
                        $("<div />", {class: "parts bold mini-marginbottom dialog-index-header", text: Header[id]}).appendTo(left);

                        var subcontainer = $("<div />", {class: "marginleft"}).appendTo(left);
                        var indexContainer = $("<div />", {class: "marginleft"}).appendTo(left);
                        var subs = indexSubHeaders[id].split(":");
                        $.each(subs, function (ind, sub) {
                            var newsub = $("<div />", {class: "double-marginleft mini-marginbottom bold dialog-index-sub", text: sub}).appendTo(subcontainer);
                            subcontainer = $("<div />", {style: "width: 100%"}).appendTo(newsub);
                        });
                        var body = Bodytext[id].split(";");
                        $.each(body, function (ind, bdy) {
                            $("<div />", {class: "half-marginbottom dialog-index-body", text: bdy}).appendTo(indexContainer).hide();
                        });

                        $("<div />", {class: "parts right quarterwide", text: "N/A"}).appendTo(item);
                        $("<div />", {style: "clear: both"}).appendTo(item);
                        counted++;
                    });
                    GetDialogIndexList(searchtext, counted, objectid, object, section);
                    dialogFunctions(section);
                } else {
                    if (counted === 0) {
                        $("<div />", {text: "No items to display"}).appendTo(parent);
                    }
                    hideDialogLoader();
                }
            }
        }
    });
}
function GetDialogObjectList(objectName, cap, searchtext, counted, objectid, object, section) {
    showDialogLoader("Loading "+object+" List.....");
    $.ajax({
        url: extension + 'BookServlet',
        type: 'GET',
        data: {
            type: "Get"+cap,
            search: searchtext,
            count: counted,
            linkedObjectId: objectid,
            linkedObject: object,
            object: objectName
        },
        success: function (data) {
            if (current === objectName) {
                var parent = $("#dialog_content");
                if (counted === 0)
                    parent.empty();
                if (data !== "none") {
                    var Ids = data[0];
                    var Names = data[1];
                    var icon = data[2];
                    var Date = data[2];
                    $.each(Ids, function (index, id) {
                        var item = $("<div />", {class: "dialog_list_item dialog_item dialog_search_item dialog_"+objectName}).appendTo(parent);
                        $("<div />", {class: "dialog_"+objectName+"_id", text: id}).appendTo(item).hide();
                        var left = $("<div />", {class: "parts twothirdswide left"}).appendTo(item);
                        $("<i />", {class: "fa "+icon+" fa-fw mini-marginright"}).appendTo(left);
                        $("<div />", {class: "parts dialog_name", text: Names[id]}).appendTo(left);
                        $("<div />", {class: "parts right quarterwide", text: Date[id]}).appendTo(item);
                        $("<div />", {style: "clear: both"}).appendTo(item);
                        counted++;
                    });
                    GetDialogObjectList(objectName, searchtext, counted, objectid, object, section);
                    dialogFunctions(section);
                } else {
                    if (counted === 0) {
                        $("<div />", {text: "No items to display"}).appendTo(parent);
                    }
                    hideDialogLoader();
                }
            }
        }
    });
}

function loadWriteDialog(action, objects) {
    $(".dialog_cover").removeClass("hide").fadeIn(200);
    $(".close_dialog").click(function () {
        $(".dialog_cover").fadeOut(200).addClass("hide");
        clearDialog();
    });
    var loaded = 0;
    $.each(objects, function (index, object){
        var section = "";
        if (objects.includes("section"))
            section = "section";
        if (loaded === 0) {
            loadDialogObject(object, section);
            loaded = 1;
        }
        $("#dialog_"+object+"link").removeClass("dialog_disabled");
        $("#dialog_"+object+"link").unbind("click").click(function () {
            loadDialogObject(object, section);
        });
    });
    $("#dialog_select").unbind("click").click(function () {
        if (selectedId === 0 || selectedId === "") {
            if (action === "CreateObject") {
                linkToFunction(action);
                clearDialog();
                $(".dialog_cover").fadeOut(200).addClass("hide");
            } else if (action === "IndexObject") {
                linkToFunction(action);
                clearDialog();
                $(".dialog_cover").fadeOut(200).addClass("hide");
            } else {
                CustomAlert("No file Selected");
            }
        } else {
            linkToFunction(action);
            clearDialog();
            $(".dialog_cover").fadeOut(200).addClass("hide");
        }
    });
}
function loadDialogObject(object, section) {
    var cap = capitaliseFirstLetter(object);
    $("#dialog_file_name").val("All "+cap);
    $(".dialog_object").removeClass("dialog_selected");
    $("#dialog_"+object+"link").addClass("dialog_selected");
    current = object;
    if(object === "indexes") {
        GetDialogIndexList("", 0, 0, "", section);
    } else {
        GetDialogObjectList(object, cap, "", 0, 0, "", section);
    }    
    setDialogTrail("All "+cap, object, loadDialogObject(object, section));
}
function dialogFunctions(section) {
    $(".dialog_item").unbind("click").click(function () {
        $(".dialog_item").removeClass("dialog_selected");
        $(this).addClass("dialog_selected");
        var name = $(this).find(".dialog_name").text();
        $("#dialog_file_name").val(name);
        selectedId = $(this).find(".dialog_book_id").text();
        if (selectedId) {
            selectedType = "Book";
        } else {
            selectedId = $(this).find(".dialog_section_id").text();
            selectedType = "Section";
        }
    });
    $(".dialog_book").unbind("dblclick").dblclick(function () {
        if (section === "section") {
            var bookid = $(this).find(".dialog_book_id").text();
            GetDialogSectionList("", 0, bookid, "Book", section);
            current = "sections";
            var fullname = $(this).find(".dialog_name").text();
            if (fullname.length > 10)
                var name = fullname.substring(0, 17) + "...";
            addToDialogTrail(name, "book", "", fullname);
            //            clearDialog();
        }
    });
    $(".dialog_section").unbind("dblclick").dblclick(function () {
        if (section === "section") {
            var bookid = $(this).find(".dialog_section_id").text();
            GetDialogSectionList("", 0, bookid, "Section", section);
            current = "sections";
            var fullname = $(this).find(".dialog_name").text();
            if (fullname.length > 10)
                var name = fullname.substring(0, 17) + "...";
            addToDialogTrail(name, "list-ul", "", fullname);
            //            clearDialog();
        }
    });
    $(".dialog_index_item").unbind("click").click(function () {
        var header = $(this).find(".dialog-index-header").text();
        var completeIndex = header;
        var subs = $(this).find(".dialog-index-sub");
        var bdys = $(this).find(".dialog-index-body");
        var bdy = "";
        $.each(subs, function (ind, sub) {
            var txt = $(this).clone().children().remove().end().text();
            completeIndex += ":" + txt;
        });
        $.each(bdys, function (ind, bd) {
            var txt = $(this).text();
            bdy += txt + "; ";
        });
        bdy = bdy.replace(/;\s*$/, "");
        $("#headers").val(completeIndex);
        $("#bodytext").val(bdy);
        selectedId = $(this).find(".dialog_index_id").text();
        selectedType = "index";
    });
    $(".dialog_index_header_item").unbind("click").click(function () {
        var clickedText = $(this).find(".dialog_name").text();
        var headerText = $("#headers").val().trim();
        if (headerText === "") {
            $("#headers").val(clickedText);
        } else {
            var newText = headerText + ":" + clickedText;
            $("#headers").val(newText);
        }
        selectedId = 1;
    });
    $(".dialog_tag_item").unbind("click").click(function () {
        var clickedText = $(this).find(".dialog_name").text();
        var clickedId = $(this).find(".dialog_tag_id").text();
        var selectedParent = $("#selectedTags");
        var tag = $("<div />", {class: "tag half-marginright mini-margin-vertical mediumtext"}).appendTo(selectedParent);
        $("<span />", {text: clickedText, class: "marginright"}).appendTo(tag);
        $("<span />", {text: clickedId, class: "tagid"}).appendTo(tag).hide();
        $("<span />", {class: "fa fa-times fa-fw cursor redtext", click: function () {
                tag.remove();
                if (selectedParent.find(".tag")[0]) {
                }
                else {
                    selectedId = 0;
                }
            }}).appendTo(tag);
        selectedId = 1;
    });
    $(".dialog_search_txt").unbind("keyup").keyup(function () {
        var searchtext = $(this).val().trim();
        if (searchtext !== "") {
            var searchObjs = $(".dialog_search_item");
            searchObjs.hide();
            $.each(searchObjs, function (index, item) {
                var txt = $(item).find(".dialog_name").text().trim().toUpperCase();
                if (txt.startsWith(searchtext.toUpperCase())) {
                    $(item).show();
                    $(item).addClass("starts");
                }
            });
            var others = $(".dialog_search_item").not(".starts");
            $.each(others, function (index, item2) {
                var txt2 = $(item2).find(".dialog_name").text().trim().toUpperCase();
                if (txt2.includes(searchtext.toUpperCase())) {
                    $(item2).remove().appendTo("#dialog_content").show();
                }
            });
            $(".dialog_search_item").removeClass("starts");
        } else {
            $(".dialog_search_item").show();
            sortElements(".dialog_search_item", ".dialog_name", "#dialog_content");
        }
    });
}
function sortElements(elements, textContainers, parent) {
    var lis = $(elements);
    var vals = [];

    $.each(lis, function (ind, ite) {
        vals.push($(ite).find(textContainers).text().toLowerCase());
    });

    vals.sort();
//    if (sortDescending)
    vals.reverse();
    for (var i = 0, l = vals.length; i < l; i++) {
        $(textContainers).filter(function () {
            return $(this).text().toLowerCase() === vals[i];
        }).closest(elements).prependTo(parent);
    }
}
function showDialogLoader(loaderText) {
    $(".dialog_loader").text(loaderText);
    $(".dialog_loader").show();
}
function hideDialogLoader() {
    $(".dialog_loader").text("");
    $(".dialog_loader").hide();
}
function setDialogTrail(object, icon, action) {
    var parent = $("#dialog_trail");
    parent.empty();
    var box = $("<div />", {text: object, class: "parts cursor dialog_list_item half-marginright", click: function () {
            action();
        }}).appendTo(parent);
    $("<i />", {class: "half-marginright fa fa-fw fa-" + icon}).prependTo(box);
}
function addToDialogTrail(object, icon, action, name) {
    var parent = $("#dialog_trail");
    var box = $("<div />", {text: object, class: "parts cursor dialog_list_item half-marginright tooltipParent", click: function () {
            action();
        }}).appendTo(parent);
    $("<i />", {class: "half-marginright fa fa-fw fa-" + icon}).prependTo(box);
    $("<div />", {class: "tooltip bottomtip hide normaltext", text: name}).appendTo(box).hide();
    tooltip();
}

function clearDialog() {
    selectedType = "";
    selectedId = 0;
    current = "";
    $("#dialog_content").empty();
    $(".txt_dialog").val("");
    $(".div_dialog").empty();
    $(".dialog-extra").addClass("hide");
    $(".dialog-extra").hide();
    $(".dialog-filename").show();
    $(".dialog_object").addClass("dialog_disabled");
    $(".dialog_object").removeClass("dialog_selected");
    $("#dialog_select_title").text("file name:");
    $("#dialog_select").text("Select");
}
function showCommentBox() {
    var userid = $("#userid").val();
    if (userid === "null" || userid === "") {
        CustomAlert("You have to login to Post a comment");
    } else {
        $("#commentSpace").removeClass("hide");
        $("#commentSpace").show();
        $("#commentTxt").focus();
    }
}