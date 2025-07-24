import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAutores } from 'app/entities/autore/autore.reducer';
import { createEntity, getEntity, updateEntity } from './libro.reducer';

export const LibroUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const autores = useAppSelector(state => state.autore.entities);
  const libroEntity = useAppSelector(state => state.libro.entity);
  const loading = useAppSelector(state => state.libro.loading);
  const updating = useAppSelector(state => state.libro.updating);
  const updateSuccess = useAppSelector(state => state.libro.updateSuccess);

  const handleClose = () => {
    navigate('/libro');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getAutores({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.prezzo !== undefined && typeof values.prezzo !== 'number') {
      values.prezzo = Number(values.prezzo);
    }

    const entity = {
      ...libroEntity,
      ...values,
      autore: autores.find(it => it.id.toString() === values.autore?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...libroEntity,
          autore: libroEntity?.autore?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="libraryApp.libro.home.createOrEditLabel" data-cy="LibroCreateUpdateHeading">
            Create or edit a Libro
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="libro-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Titolo"
                id="libro-titolo"
                name="titolo"
                data-cy="titolo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 200, message: 'This field cannot be longer than 200 characters.' },
                }}
              />
              <ValidatedField
                label="Prezzo"
                id="libro-prezzo"
                name="prezzo"
                data-cy="prezzo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="libro-autore" name="autore" data-cy="autore" label="Autore" type="select" required>
                <option value="" key="0" />
                {autores
                  ? autores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/libro" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default LibroUpdate;
