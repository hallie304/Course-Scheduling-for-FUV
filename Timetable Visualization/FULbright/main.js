console.log("innit");

	// Here nothing has been assigned
	// while creating Date object
	var dateobj =
	new Date('October 13, 1996 07:35:32 ');

	// Contents of above date object is
	// converted into a string using toISOString() method.
	var B = dateobj.toISOString();

	// Printing the converted string.
	console.log(B);

$(function(){
  var scheduler = $("#scheduler").dxScheduler({
      dataSource: data,
      views: ["day", "week", "timelineDay"],
      currentView: "day",
      currentDate: new Date(2022, 2, 15),
      firstDayOfWeek: 0,
      startDayHour: 7,
      endDayHour: 23,
      showAllDayPanel: false,
      height: 600,
      groups: ["classroomId"],
      crossScrollingEnabled: true,
      cellDuration: 20,
      editing: { 
          allowAdding: false
      },
      resources: [{ 
          fieldExpr: "courseId",
          dataSource: courseData,
          useColorAsDefault: true
      }, { 
          fieldExpr: "classroomId", 
          dataSource: classroomData
      }],
      appointmentTooltipTemplate: function(data, container) {
          var markup = getTooltipTemplate(getcourseById(data.courseId));
          markup.find(".edit").dxButton({
              text: "Edit details",
              type: "default",
              onClick: function() {
                  scheduler.showAppointmentPopup(data, false);
              }
          });
          
                     
          let buttonDelete = $('<div>').dxButton({
            icon: 'trash',
            hint: 'Remove order From the Scheduer',
             onClick: function(e) { // Click event
               e.event.stopPropagation();
              var promiseTest = new Promise(function(resolve, reject) {
                setTimeout(function() {
                resolve('foo');
                }, 300);
              });
              promiseTest.then(() => {
                scheduler.deleteAppointment(data, false);
                scheduler.hideAppointmentTooltip();
              }).catch((err) => {
                console.log(err)
              });
            }
          });
          
          markup.append(buttonDelete);
          return markup;
      },
      appointmentTemplate: function(data, classroomData) { 
       
          var courseInfo = getcourseById(data.courseId) || {};
          console.log(courseInfo);
          var classroomInfo= getclassroomById(classroomData.id) || {};
          console.log(classroomInfo);
      
          return $("<div class='showtime-preview'>" + 
                      "<div>" + courseInfo.text + "</div>" +
                      
                      "<div>Capacity : <strong>" + classroomData.capacity + " students</strong>" +
                      "</div>" + 
                      "<div>" + Globalize.formatDate(data.startDate, { time: "short" }) +
                          " - " + Globalize.formatDate(data.endDate, { time: "short" }) +
                      "</div>" + 
                    
                      "<div>Instructor : <strong>" + courseInfo.instructor + " </strong>" +

                   "</div>"); 
   
      },
      onAppointmentFormOpening: function(data) {
          var form = data.form,
              courseInfo = getcourseById(data.appointmentData.courseId) || {},
              startDate = data.appointmentData.startDate;
  
              form.option("items", [{
                  label: {
                      text: "course"
                  },
                  editorType: "dxSelectBox",
                  dataField: "courseId",
                  editorOptions: {
                      items: courseData,
                      displayExpr: "text",
                      valueExpr: "id",
                      onValueChanged: function(args) {
                          courseInfo = getcourseById(args.value);
                          form.getEditor("instructor")
                              .option("value", courseInfo.instructor);
                          form.getEditor("endDate")
                              .option("value", new Date (startDate.getTime() +
                                  60 * 1000 * courseInfo.duration));
                      }
                  },
              }, {
                  label: {
                      text: "instructor"
                  },
                  name: "instructor",
                  editorType: "dxTextBox",
                  editorOptions: {
                      value: courseInfo.instructor,
                      readOnly: true
                  }
              }, {
                  dataField: "startDate",
                  editorType: "dxDateBox",
                  editorOptions: {
                      width: "100%",
                      type: "datetime",
                      onValueChanged: function(args) {
                          startDate = args.value;
                          form.getEditor("endDate")
                              .option("value", new Date (startDate.getTime() +
                                  60 * 1000 * courseInfo.duration));
                      }
                  }
              }, {
                  name: "endDate",
                  dataField: "endDate",
                  editorType: "dxDateBox",
                  editorOptions: {
                      width: "100%",
                      type: "datetime",
                      readOnly: true
                  }
              }, {
                  dataField: "capacity",
                  editorType: "dxRadioGroup",
                  editorOptions: {
                      dataSource: [5, 10, 15, 20],
                      itemTemplate: function(itemData) {
                          return "$" + itemData;
                      }
                  }
              }
          ]);
      }
  }).dxScheduler("instance");
  
  function getcourseById(id) {
      return DevExpress.data.query(courseData)
              .filter("id", id)
              .toArray()[0];
  }

  function getclassroomById(id) {
    return DevExpress.data.query(classroomData)
            .filter("id", id)
            .toArray()[0];
}
  
  function getTooltipTemplate(courseData) {
    console.log(courseData.instructor);
    console.log(courseData.text);
      return $("<div class='course-tooltip'>" +
                  
                  "<div class='course-info'>" +
                      "<div class='course-title'>" +
                          courseData.text + " (" + courseData.year + ")" +
                      "</div>" + 
                      "<div>" +
                          "instructor: " + courseData.instructor +
                      "</div>" +
                      "<div>" +
                          "Duration: " + courseData.duration + " minutes" +
                      "</div>" +
                  "</div><br />" +
                  "<div class='edit'></div>" +
               "</div>");
  }
});

var courseData = [{
    id: "ART101",
    text: "Introduction to Visual Studies",
    instructor: "Tram Luong",
    duration: 90,
    color: "#cb6bb2"
  }, {
    id: "CORE202",
    text: "Global Humanities and Social Change",
    instructor: "Mathew McDonald",
    duration: 60,
    color: "#56ca85"
  }, {
    id: "CORE303",
    text: "Design and Systems Thinking",
    instructor: "	Truong Trung Kien",
    duration: 90,
    color: "#1e90ff"
  }, {
    id: "CS101",
    text: "Computer Science I",
    instructor: "Phan Thanh Trung",
    duration: 90,
    color: "#ff9747"
  }, {
    id: "ECON202",
    text: "Behavioural and Experimental Economics",
    instructor: "Graeme Walker",
    duration: 60,
    color: "#f05797"
  }, {
    id:"ECON302",
    text: "Economics of Inequality",
    instructor: "Khieu Van Hoang",
    duration: 90,
    color: "#2a9010"
  }];


var classroomData = [{
    text: "CR 1",
    id: "CR_1",
    capacity: 30
}, {
    text: "CR 2",
    id: "CR_2",
    capacity: 30
}, {
    text: "CR 3",
    id: "CR_3",
    capacity: 30
}, {
    text: "CR 4",
    id: "CR_4",
    capacity: 30
}, {
    text: "CR 5",
    id: "CR_5",
    capacity: 30
}, {
    text: "CR 6",
    id: "CR_6"
}, {
    text: "CR 7",
    id: "CR_7",
    capacity: 30
}, {
    text: "CR 8",
    id: "CR_8",
    capacity: 30
}, {
    text: "Ting",
    id: "CR_Ting",
    capacity: 30
}
];

var data = [{
    classroomId: "CR_1",
    courseId: "ECON302",
    startDate: new Date(2022, 02, 15, 8, 00),
    endDate: new Date(2022, 02, 15, 10, 10)
}, {
    classroomId: "CR_2",
    courseId: "ECON202",
    startDate: new Date(2022, 03, 15, 13, 00),
    endDate: new Date(2022, 02, 15, 14, 10)
}, 
{
    classroomId: "CR_3",
    courseId: "CS101",
    startDate: new Date(2022, 02, 16, 8, 00),
    endDate: new Date(2022, 02, 16, 10, 10)
}, {
    classroomId: "CR_8",
    courseId: "CORE303",
    startDate: new Date(2022, 02, 16, 15, 00),
    endDate: new Date(2022, 02, 16, 17, 30)
}, {
    classroomId: "CR_Ting",
    courseId: "ECON202",
    startDate: new Date(2022, 02, 13, 8, 00),
    endDate: new Date(2022, 02, 13, 10, 10)
}, {
    classroomId: "CR_3",
    courseId: "CS101",
    startDate: new Date(2022, 02, 15, 10, 15),
    endDate: new Date(2022, 02, 15, 11, 15)
}, 
{
    classroomId: "CR_4",
    courseId: "CORE303",
    startDate: new Date(2022, 02, 19, 8, 00),
    endDate: new Date(2022, 02, 19, 10, 10)
}, {
    classroomId: "CR_3",
    courseId: "CS101",
    startDate: new Date(2022, 02, 19, 13, 00),
    endDate: new Date(2022, 02, 19, 14, 10)
}, {
    classroomId: "CR_3",
    courseId: "ECON202",
    startDate: new Date(2022, 02, 19, 16, 00),
    endDate: new Date(2022, 02, 19, 17, 10)
} 
 ]



