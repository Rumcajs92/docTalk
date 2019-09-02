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
// @material-ui/icons
// core components
import withStyles from "@material-ui/core/styles/withStyles";
import dashboardStyle from "../../assets/jss/material-dashboard-react/views/dashboardStyle";
import GridContainer from "../../components/Grid/GridContainer";
import Card from "../../components/Card/Card";
import CardHeader from "../../components/Card/CardHeader";
import CardBody from "../../components/Card/CardBody";
import api from "../../api";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/core/SvgIcon/SvgIcon";
import Typography from "@material-ui/core/Typography";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import { Link } from "react-router-dom";
import RegularButton from "../../components/CustomButtons/Button";
import { FiberManualRecord, Stop } from "@material-ui/icons";
import { ReactMic } from "react-mic";
import ReactPlayer from "react-player";
import ButtonGroup from "@material-ui/core/ButtonGroup";
import GridItem from "../../components/Grid/GridItem";
import Grid from "@material-ui/core/Grid";

class AdaptationRecorder extends React.Component {
  constructor(props) {
    super(props);
    let state = props.location.state;
    this.state = {
      adaptationId: props.match.params.adaptationId,
      adaptation: state ? props.location.state.adaptation : null,
      selectedTranscriptionId: null,
      record: false,
      transcriptionRecordingMap: state
        ? props.location.state.adaptation.transcriptions.map(transcription => {
            return { transcription: transcription, recording: null };
          })
        : null
    };

    this.selectTranscription = this.selectTranscription.bind(this);
  }

  componentDidMount() {
    if (this.state.adaptation) {
      return;
    }
    fetch(api.adaptation(this.state.adaptationId))
      .then(resp => resp.json())
      .then(data =>
        this.setState({
          adaptation: data,
          transcriptionRecordingMap: data.transcriptions.map(transcription => {
            return { transcription: transcription, recording: null };
          })
        })
      );
  }

  selectTranscription = id => {
    this.setState({
      selectedTranscriptionId: id
    });
    let selectedTranscriptionRecording = this.state.transcriptionRecordingMap.find(
      traRec => traRec.transcription.id === id
    );

    const voiceRecordingFiles =
      selectedTranscriptionRecording.transcription.voiceRecordingFiles;
    if (
      voiceRecordingFiles.length > 0 &&
      !selectedTranscriptionRecording.recording
    ) {
      fetch(
        api.getFile(
          selectedTranscriptionRecording.transcription.voiceRecordingFiles[0]
            .path
        )
      )
        .then(response => response.blob())
        .then(blob => {
          const transRecs = this.state.transcriptionRecordingMap.map(
            transRec => {
              if (transRec.transcription.id === id) {
                return {
                  transcription: transRec.transcription,
                  recording: {
                    blob: blob,
                    blobURL: URL.createObjectURL(blob)
                  }
                };
              }
              return transRec;
            }
          );
          console.log(transRecs);
          this.setState({
            transcriptionRecordingMap: transRecs
          });
        });
    }
  };

  onStop = recordedBlob => {
    const transcriptionRecordingMap = this.state.transcriptionRecordingMap.map(
      transcriptionRecording => {
        if (
          transcriptionRecording.transcription.id ===
          this.state.selectedTranscriptionId
        ) {
          return {
            transcription: transcriptionRecording.transcription,
            recording: recordedBlob
          };
        }
        return transcriptionRecording;
      }
    );
    this.setState({
      transcriptionRecordingMap: transcriptionRecordingMap
    });
    const formData = new FormData();
    formData.append("file", recordedBlob.blob, "file");
    fetch(
      api.storeTranscriptionRecording(
        this.state.adaptationId,
        this.state.selectedTranscriptionId
      ),
      {
        method: "POST",
        body: formData
      }
    )
      .then(resp => resp.json())
      .then(json => {
        console.log("Voice Recording File: ");
        console.log(json);
      });
  };

  startRecording = () => {
    if (this.state.selectedTranscriptionId) {
      this.setState({
        record: true
      });
    }
  };

  stopRecording = () => {
    this.setState({
      record: false
    });
  };

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
            <CardBody>
              <Grid xs={12} sm={12} md={12}>
                <Grid xs={12} sm={12} md={12}>
                  <ReactMic
                    record={this.state.record}
                    visualSetting="frequencyBars"
                    onStop={this.onStop}
                    strokeColor="#000000"
                    backgroundColor="#FF4081"
                  />
                </Grid>
                <Grid xs={12} sm={12} md={12}>
                  <RecordingToolBar
                    record={this.state.record}
                    startRecord={this.startRecording}
                    stopRecord={this.stopRecording}
                  />
                </Grid>
                <Grid xs={12} sm={12} md={12} className={classes.scrollable}>
                  <TranscriptionList
                    classes={classes}
                    transcriptionRecordingMap={
                      this.state.transcriptionRecordingMap
                    }
                    selectedTranscriptionId={this.state.selectedTranscriptionId}
                    onTranscriptionClick={this.selectTranscription}
                  />
                </Grid>
              </Grid>
            </CardBody>
          </Card>
        </GridContainer>
      </div>
    );
  }
}

function TranscriptionItem(props) {
  const recordedPointerClass = props.isRecorded
    ? props.classes.recorded
    : props.classes.notRecorded;
  const config = {
    file: {
      forceAudio: true,
      attributes: {
        controls: true
      }
    }
  };
  return (
    <ExpansionPanel
      expanded={props.selected === props.transcription.id}
      onClick={props.onTranscriptionClick}
      className={recordedPointerClass}
    >
      <ExpansionPanelSummary
        expandIcon={<ExpandMoreIcon />}
        aria-controls="panel1a-content"
        id="panel1a-header"
      >
        <Typography className={props.classes.heading}>
          {props.transcription.transcriptionText
            .replace("<s>", "")
            .replace("</s>", "")}
        </Typography>
      </ExpansionPanelSummary>
      <ExpansionPanelDetails>
        <GridContainer>
          <GridItem xs={12} sm={12} md={12}>
            <div>id: {props.transcription.id}</div>
            <div>recording: {!!props.recording}</div>
          </GridItem>
          <GridItem xs={12} sm={12} md={12}>
            <Player recording={props.recording} config={config} />
          </GridItem>
        </GridContainer>
      </ExpansionPanelDetails>
    </ExpansionPanel>
  );
}

function Player(props) {
  if (!props.recording) {
    return null;
  }
  return (
    <div width="100%" height="100%">
      <ReactPlayer
        playing={false}
        url={props.recording.blobURL}
        controls={true}
        height={"30px"}
        config={props.config}
      />
    </div>
  );
}

function RecordingToolBar(props) {
  const recordOrPlay = props.record ? (
    <RegularButton color={"info"} onClick={props.stopRecord}>
      <Stop /> Stop
    </RegularButton>
  ) : (
    <RegularButton color={"danger"} onClick={props.startRecord}>
      <FiberManualRecord /> Record
    </RegularButton>
  );
  return <ButtonGroup>{recordOrPlay}</ButtonGroup>;
}

function TranscriptionList(props) {
  // eslint-disable-next-line react/jsx-no-undef
  if (!props.transcriptionRecordingMap) {
    return null;
  }
  return props.transcriptionRecordingMap.map(transcriptionRecording => {
    const voiceRecordingFiles = transcriptionRecording.transcription
      .voiceRecordingFiles
      ? transcriptionRecording.transcription.voiceRecordingFiles
      : [];
    return (
      <TranscriptionItem
        key={transcriptionRecording.transcription.id}
        classes={props.classes}
        transcription={transcriptionRecording.transcription}
        isRecorded={
          voiceRecordingFiles.length > 0 || !!transcriptionRecording.recording
        }
        recording={transcriptionRecording.recording}
        selected={props.selectedTranscriptionId}
        onTranscriptionClick={() =>
          props.onTranscriptionClick(transcriptionRecording.transcription.id)
        }
      />
    );
  });
}

AdaptationRecorder.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(AdaptationRecorder);
