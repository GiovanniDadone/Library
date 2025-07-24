import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './libro.reducer';

export const LibroDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const libroEntity = useAppSelector(state => state.libro.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="libroDetailsHeading">Libro</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{libroEntity.id}</dd>
          <dt>
            <span id="titolo">Titolo</span>
          </dt>
          <dd>{libroEntity.titolo}</dd>
          <dt>
            <span id="prezzo">Prezzo</span>
          </dt>
          <dd>{libroEntity.prezzo}</dd>
          <dt>Autore</dt>
          <dd>{libroEntity.autore ? libroEntity.autore.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/libro" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/libro/${libroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LibroDetail;
