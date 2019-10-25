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
import SnackbarContent from "components/Snackbar/SnackbarContent.jsx";
// @material-ui/icons
// core components

import dashboardStyle from "assets/jss/material-dashboard-react/views/dashboardStyle.jsx";
import ReactMic from "../../components/ReactMicExtended/es/components/ReactMic";
import { Stomp } from "@stomp/stompjs/esm5/compatibility/stomp";
import api from "../../api";

import { encodeWavFile } from "wav-file-encoder";

import resampler from "audio-resampler";

class Editor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      record: false,
      rec: null
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
        .then(resp => resp.json())
        .then(json => {
          console.log("Voice Recording File: ");
          console.log(json);
        });
    });
  }

  render() {
    // const { classes } = this.props;
    return (
      <div>
        <ReactMic
          record={this.state.record}
          className="sound-wave"
          onStop={this.onStop}
          strokeColor="#000000"
          backgroundColor="#FF4081"
          // mimeType={"audio/wav"}
        />
        <button onClick={this.startRecording} type="button">
          Start
        </button>
        <button onClick={this.stopRecording} type="button">
          Stop
        </button>
      </div>
    );
  }
}

Editor.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(Editor);
