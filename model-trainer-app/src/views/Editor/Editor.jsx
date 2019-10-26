/*!

=========================================================
* Material Dashboard React - v1.7.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/material-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
// nodejs library to set properties for components
import PropTypes from "prop-types";
// react plugin for creating charts
// @material-ui/core
import withStyles from "@material-ui/core/styles/withStyles";
import dashboardStyle from "assets/jss/material-dashboard-react/views/dashboardStyle.jsx";
import ReactMic from "../../components/ReactMicExtended/es/components/ReactMic";
import api from "../../api";
import { encodeWavFile } from "wav-file-encoder";
import resampler from "audio-resampler";
import Grid from "@material-ui/core/Grid";
import GridContainer from "../../components/Grid/GridContainer";
import Card from "../../components/Card/Card";
import CardHeader from "../../components/Card/CardHeader";
import RecordingToolbar from "../../components/RecordingToolbar/RecordingToolbar";
import CKEditor from "@ckeditor/ckeditor5-react";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";

// @material-ui/icons
// core components

class SpeechEditor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      record: false,
      text: "Hello sphinx"
    };
    this.onStop = this.onStop.bind(this);
  }

  timeout = 250;

  startRecording = () => {
    this.setState({
      record: true
    });
  };

  stopRecording = () => {
    this.setState({
      record: false
    });
  };

  onStop(recordedBlob) {
    resampler(recordedBlob.blobURL, 16000, event => {
      const formData = new FormData();
      let arrayBuffer = encodeWavFile(event.getAudioBuffer(), 0);
      let resampledBlob = new Blob([arrayBuffer], { type: "audio/wav" });
      formData.append("file", resampledBlob, "file");
      fetch(api.getResultFromAdaptedModel(1), {
        method: "PUT",
        body: formData
      })
        .then(resp => resp.text())
        .then(text => {
          console.log("Voice Recording File: ");
          console.log(text);
          let newText = this.state.text + text;
          this.setState({
            text: newText
          });
        });
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <div>
        <GridContainer>
          <Card>
            <CardHeader color="primary">
              <h4 className={classes.cardTitleWhite}>
                Adaptation Transcriptions
              </h4>
            </CardHeader>
            <ReactMic
              record={this.state.record}
              className="sound-wave"
              onStop={this.onStop}
              strokeColor="#000000"
              backgroundColor="#FF4081"
              // mimeType={"audio/wav"}
            />
            <Grid xs={12} sm={12} md={12}>
              <RecordingToolbar
                record={this.state.record}
                startRecord={this.startRecording}
                stopRecord={this.stopRecording}
              />
            </Grid>
            <Grid xs={12} sm={12} md={12}>
              <CKEditor
                editor={ClassicEditor}
                data={this.state.text}
                onInit={editor => {
                  // You can store the "editor" and use when it is needed.
                  console.log("Editor is ready to use!", editor);
                }}
                onChange={(event, editor) => {
                  const data = editor.getData();
                  this.setState({
                    text: data
                  })
                  console.log({ event, editor, data });
                }}
                onBlur={(event, editor) => {
                  console.log("Blur.", editor);
                }}
                onFocus={(event, editor) => {
                  console.log("Focus.", editor);
                }}
              />
            </Grid>
          </Card>
        </GridContainer>
      </div>
    );
  }
}

SpeechEditor.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(SpeechEditor);
